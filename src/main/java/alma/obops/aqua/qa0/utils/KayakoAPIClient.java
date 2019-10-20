package alma.obops.aqua.qa0.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import alma.obops.aqua.qa0.domain.KayakoTicket;

/**
 * Client for the REST API of Kayako,  
 * see https://kayako.atlassian.net/wiki/display/DEV/Kayako+REST+API
 * @author amchavan, 28-Oct-2015
 */

public class KayakoAPIClient {

	private static final String API_PATH = "/hdproxy/api";
	//private static final String TICKET_CREATION_PATH = "/Tickets/SnoopyTicket";
	//private static final String TICKET_SEARCH_PATH = "/Tickets/SnoopyTicketSearch";
	//private static final String TICKET_STATUS_PATH = "/Tickets/SnoopyTicketStatus";

    private static final String TICKET_CREATION_ENDPOINT = "/Tickets/Ticket";
    private static final String TICKET_SEARCH_ENDPOINT = "/Tickets/TicketSearch";
    private static final String TICKET_STATUS_ENDPOINT = "/Tickets/TicketStatus";

    private static final String TICKET_CREATION_PATH = "/kayakopost";
    private static final String TICKET_SEARCH_PATH = "/kayakosearch";
    private static final String TICKET_STATUS_PATH = "/kayakoget";
	private static final Logger logger = 
			Logger.getLogger( KayakoAPIClient.class.getSimpleName() );

	private String kayakoURL;
	private String apiKey;
	private String secretKey;

	private Map<String,String> statusIdToTitle;
	
	/** Generates a new random string with every call */
	private static String computeNewSaltString() {
		return UUID.randomUUID().toString();
	}
	
	private static Document convertResponseToXML( String method, URI uri, CloseableHttpResponse response ) throws IOException, DocumentException {

		final int statusCode = response.getStatusLine().getStatusCode();
//		System.out.println( ">>> statusCode=" + statusCode );
		if( statusCode != HttpURLConnection.HTTP_OK ) {
			String msg = method + " failed: " + uri + " -- status=" + statusCode;
			logger.severe( msg );
			throw new RuntimeException( msg );
		}
		
		Document xml = null;
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				try {
					xml = toXMLDocument( instream );
				} 
				finally {
					instream.close();
				}
				EntityUtils.consume( entity );	// probably useless
			}
		} 
		finally {
			response.close();
		}
		
		return xml;
	}
	
	private static Document executeRequest( CloseableHttpClient httpclient, HttpUriRequest request ) throws IOException {
		try {
			CloseableHttpResponse response = httpclient.execute( request );
			return convertResponseToXML( request.getMethod(),
						                         request.getURI(),
						                         response );
		}
		catch( Exception e ) {
			throw new RuntimeException( e );
		}
		finally {
			httpclient.close();
		}
	}
	
	/** See https://github.com/kayako/dotnet-api-library/blob/master/src/KayakoRestAPI/Utilities/UnixTimeUtility.cs */
	public static Calendar fromKayakoUnixTime( int unitTime ) {
		Calendar c = Calendar.getInstance();
		c.setTime( new Date( 0 ));
		c.add( Calendar.SECOND, unitTime);
		return c;
	}
	
	/** See https://kayako.atlassian.net/wiki/display/DEV/Generating+an+API+Signature#GeneratinganAPISignature-JavaExample */
	private static String generateHmacSHA256Signature( String key, String salt ) throws GeneralSecurityException {
		byte[] hmacData = null;

		try {
			SecretKeySpec secretKey = new SecretKeySpec( key.getBytes( "UTF-8" ), "HmacSHA256" );
			Mac mac = Mac.getInstance( "HmacSHA256" );
			mac.init( secretKey );
			hmacData = mac.doFinal( salt.getBytes( "UTF-8" ));
			Base64.Encoder encoder = Base64.getEncoder();
			byte[] bytes = encoder.encode( hmacData );
			return new String( bytes );
		} 
		catch (UnsupportedEncodingException e) {
			throw new GeneralSecurityException( e );
		}
	}

	private static String generateUrlEncodedSignature( String secretKey, String salt )
			throws GeneralSecurityException, UnsupportedEncodingException {
		
		String generateHmacSHA256Signature = generateHmacSHA256Signature( secretKey, salt  );
		return URLEncoder.encode( generateHmacSHA256Signature, "UTF-8" );
	}

	private static Document toXMLDocument( InputStream stream ) throws DocumentException {
		SAXReader reader = new SAXReader();
		return reader.read( stream );
	}
	
	
	/** Needed by Spring, do not use */
	public KayakoAPIClient() {
	}

	public KayakoAPIClient( String kayakoURL, String apiKey, String secretKey ) throws Exception {
		this.kayakoURL = kayakoURL; 
		this.apiKey = apiKey; 
		this.secretKey = secretKey;

//		Functionality switched off temporary 		

//		try{
//			this.statusIdToTitle = doTicketStatus();
//		} catch (Exception e){
//			logger.severe( "Kayako server request failed" );
//		}
//		
//		String msg = "Created instance of "
//				+ this.getClass().getSimpleName()
//				+ " on "
//				+ this.kayakoURL;		
//		logger.info( msg );
	}
	
	/**
	 * Generic Kayako REST API call: GET
	 * 
	 * @param endpoint
	 *            Something like <code>/Tickets/Ticket/3310</code>
	 * 
	 * @return An XML document, if all goes well. For instance, when
	 *         {@link endpoint} is <code>/Tickets/Ticket/3310</code>, this method will GET
	 *         the following URL<br>
	 *         <code>https://sus-dev.nrao.edu/staging_alma_455/api/index.php?e=/Tickets/Ticket/3310&apikey=5fa01754-81f7-c2b4-197b-f304c056efdc&salt=0123456789&signature=W8QfyvU2kbaH0qCsvW6hN8s%2BnjMro4UeRSblTsRnGaM%3D</code>
	 *         <br>
	 *         and return something like:<pre>
&lt;?xml version="1.0" encoding="UTF-8"?>
&lt;tickets>
   &lt;ticket id="3310" flagtype="0">
      &lt;displayid>&lt;![CDATA[3310]]>&lt;/displayid>
      &lt;departmentid>&lt;![CDATA[0]]>&lt;/departmentid>
      &lt;statusid>&lt;![CDATA[8]]>&lt;/statusid>
      &lt;priorityid>&lt;![CDATA[7]]>&lt;/priorityid>
      ...
	 *         </pre>
	 * 
	 * @throws Exception
	 */
	public Document doGet( String endpoint,String proxyEndpoint ) throws Exception {
		
		final String salt = computeNewSaltString();
//		System.out.println( ">>> salt: " + salt );
		final String signature = generateUrlEncodedSignature( secretKey, salt );
//		System.out.println( ">>> Encoded signature: " + signature );

		final String url = kayakoURL + API_PATH + endpoint+
                "?endpoint=" + proxyEndpoint +
                "&apikey=" + apiKey +
                "&salt=" + salt +
                "&signature=" + signature;
		
//		System.out.println( ">>> URL: " + url );
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet request = new HttpGet( url );
		
		return executeRequest( httpclient, request );
	}

	/**
	 * Generic Kayako REST API call: POST
	 * <br>
	 * See https://kayako.atlassian.net/wiki/display/DEV/Kayako+REST+API
	 * 
	 * @param endpoint
	 *            For instance: <code>/Tickets/TicketSearch</code>
	 * @param payload
	 *            A list of name/value pairs, for instance
	 *            <code>[creatoremail=1, query=user@example.com]</code>
	 * @return An XML document, if all goes well. For instance, this method will
	 *         POST a URL like<br>
	 *         <code>https://sus-dev.nrao.edu/staging_alma_455/api/index.php/Tickets/TicketSearch</code>
	 *         <br>
	 *         with a payload including <code>apikey</code>, <code>salt</code>
	 *         and <code>signature</code>; it will return something like:
	 * 
	 *         <pre>
	&lt;?xml version="1.0" encoding="UTF-8"?>
	&lt;tickets>
	&lt;ticket id="3310" flagtype="0">
	  &lt;displayid>&lt;![CDATA[3310]]>&lt;/displayid>
	  &lt;departmentid>&lt;![CDATA[0]]>&lt;/departmentid>
	  &lt;statusid>&lt;![CDATA[8]]>&lt;/statusid>
	  &lt;priorityid>&lt;![CDATA[7]]>&lt;/priorityid>
	  ...
	 *         </pre>
	 * 
	 * @throws Exception
	 */
	public Document doPost( String endpoint, List<NameValuePair> payload ) throws Exception {
		
		String url = kayakoURL + API_PATH + endpoint;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost request = new HttpPost( url );

		final String salt = computeNewSaltString();
		final String signature = generateHmacSHA256Signature( secretKey, salt );
		payload.add( new BasicNameValuePair( "apikey", apiKey ));
		payload.add (new BasicNameValuePair( "salt", salt ));
		payload.add( new BasicNameValuePair( "signature", signature ));
		UrlEncodedFormEntity encodedPayload = new UrlEncodedFormEntity( payload );
        request.setEntity( encodedPayload );
		
//		System.out.println( ">>>       url: " + url );
//		System.out.println( ">>>    apikey: " + apiKey );
//		System.out.println( ">>>      salt: " + salt );
//		System.out.println( ">>> signature: " + signature );

        ByteArrayOutputStream s = new ByteArrayOutputStream( 1024 * 1024 );
        encodedPayload.writeTo( s );
        System.out.println( ">>> encodedPayload: " + s.toString() );

		return executeRequest( httpclient, request );
	}
	
	/**
	 * Creates a new Kayako ticket.
	 * See https://kayako.atlassian.net/wiki/display/DEV/REST+-+Ticket
	 * 
	 * @return An instance of {@link KayakoTicket}
	 * @throws Exception
	 */
	public KayakoTicket doTicketCreation( 
			String subject, 			// mandatory, Ticket Subject, e.g. User Maurizio Chavan (amchavan) changed preferred ARC from EU to EA
			String fullname, 			// mandatory, Full Name of creator, e.g. Maurizio Chavan
			String email, 				// mandatory, Email Address of creator, e.g. amchavan@eso.org
			String contents, 			// mandatory, Contents of the first ticket post, e.g. Created automatically by the 'Edit Profile' Science Portal page
			String departmentid, 		// mandatory, Department ID, 16 for General Queries (EA), 17 for General Queries (EU) or 18 for General Queries (NA), depending on the old preferred ARC, see departments.png attachment
			String ticketstatusid, 		// mandatory, Ticket Status ID, 4 for Open
			String ticketpriorityid, 	// mandatory, Ticket Priority ID, 7 for Default
			String tickettypeid			// optional, should be 1, see https://kayako.atlassian.net/wiki/display/DEV/REST+-+Ticket#REST-Ticket-Arguments(POSTvariables)
		) throws Exception {

		List<NameValuePair> payload = new ArrayList<NameValuePair>();
		payload.add( new BasicNameValuePair( "autouserid", "1" )); // optional, should be 1, see https://kayako.atlassian.net/wiki/display/DEV/REST+-+Ticket#REST-Ticket-Arguments(POSTvariables)
		payload.add( new BasicNameValuePair( "subject", subject ));
		payload.add( new BasicNameValuePair( "fullname", fullname ));
		payload.add( new BasicNameValuePair( "email", email ));
		payload.add( new BasicNameValuePair( "contents", contents ));
		payload.add( new BasicNameValuePair( "departmentid", departmentid ));
		payload.add( new BasicNameValuePair( "ticketstatusid", ticketstatusid ));
		payload.add( new BasicNameValuePair( "ticketpriorityid", ticketpriorityid ));
		payload.add( new BasicNameValuePair( "tickettypeid", tickettypeid ));
        payload.add( new BasicNameValuePair( "endpoint", TICKET_CREATION_ENDPOINT ));
		
		Document xml = doPost( TICKET_CREATION_PATH, payload );
		//System.out.println( xml.asXML() );

		// xml should be non-null here
		List<?> nodes = xml.selectNodes( "//ticket" );
		List<KayakoTicket> list = parseElementsIntoTickets( nodes );
		return list.get( 0 );
	}

	/**
	 * See https://kayako.atlassian.net/wiki/display/DEV/REST+-+TicketSearch
	 * 
	 * @return A list of {@link KayakoTicket} instances
	 * @throws Exception
	 */
	public List<KayakoTicket> doTicketSearch( String email ) throws Exception {

		List<NameValuePair> payload = new ArrayList<NameValuePair>();
		payload.add( new BasicNameValuePair( "creatoremail", "1" ));
		payload.add( new BasicNameValuePair( "query", email ));
        payload.add( new BasicNameValuePair( "endpoint", TICKET_SEARCH_ENDPOINT ));
		
		Document xml = doPost( TICKET_SEARCH_PATH, payload );
		//System.out.println( xml.asXML() );
		
		// xml should be non-null here
		List<?> nodes = xml.selectNodes( "//ticket" );
		return parseElementsIntoTickets( nodes );
	}

	/**
	 * See https://kayako.atlassian.net/wiki/display/DEV/REST+-+TicketStatus
	 * 
	 * @return A map of <status-id,status-name> pairs
	 * @throws Exception
	 */
	public Map<String,String> doTicketStatus() throws Exception {
		
        Document xml = doGet( TICKET_STATUS_PATH,TICKET_STATUS_ENDPOINT );
//		System.out.println( xml.asXML() );
		
		// xml should be non-null here
		List<?> list = xml.selectNodes( "//ticketstatus" );
		Map<String,String> ret = new HashMap<String,String>();
		
		for (Object o : list) {
	
			Element ticketStatus = (Element) o;

			String id = ticketStatus.selectSingleNode( "id" ).getStringValue();
			String title = ticketStatus.selectSingleNode( "title" ).getStringValue();
			
			ret.put(id, title);
		}
		
		logger.info( "ALMA Helpdesk status values: " + ret );
		return ret;
	}

	private List<KayakoTicket> parseElementsIntoTickets(List<?> list) {
		List<KayakoTicket> ret = new ArrayList<KayakoTicket>();
		
		for (Object o : list) {

			KayakoTicket kt = new KayakoTicket();
			Element ticket = (Element) o;

			kt.setEmail( ticket.selectSingleNode( "email" ).getStringValue() );
			kt.setId( Integer.parseInt( ticket.attributeValue( "id" )) );
			kt.setSubject( ticket.selectSingleNode( "subject" ).getStringValue() );
			String statusID = ticket.selectSingleNode( "statusid" ).getStringValue();
			String statusTitle = statusIdToTitle.get( statusID );
			kt.setStatus( statusTitle != null ? statusTitle : statusID + "?" );
			kt.setUrl(kayakoURL + "/index.php?/Tickets/Ticket/View/" + kt.getId());
			ret.add( kt );
			//System.out.println( ">>> " + kt );
		}
		return ret;
	}
	
	// FOR TESTING ONLY
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		
		String kayakoURL = "https://sus-dev.nrao.edu/staging_alma_455_snoopy";
		String apiKey = "8cc3ce9a-ee7a-b294-f9a1-df45435e49dc";
		String secretKey = "YjYyMGJmOGUtMjgxZS01M2I0LWVkOTktMGQzMzI3MTU2YmQ3MzExOGYyZTctYjdjZS00ZGE0LWY1NDYtNzBkODFjNTNkZmI0";
		
		KayakoAPIClient client = new KayakoAPIClient( kayakoURL, apiKey, secretKey );
		
		String email = "akiko.kawamura@nao.ac.jp";//"lucaricci83@gmail.com";//"martin.cordiner@nasa.gov";//"pandrean@eso.org";
		List<KayakoTicket> tickets = client.doTicketSearch( email );
		
		KayakoTicket ticket = client.doTicketCreation(
				/* subject */ "User Maurizio Chavan (amchavan) changed preferred ARC from EU to EA", 
				/* fullname */ "A M Chavan",
				/* email */ "amchavan@eso.org",
				/* contents */ "Created automatically by the 'Edit Profile' Science Portal page",
				/* departmentid */ "17", 
				/* ticketstatusid */ "4", 
				/* ticketpriorityid*/ "7", 
				/* tickettypeid */ "1"
				);
	}
}
