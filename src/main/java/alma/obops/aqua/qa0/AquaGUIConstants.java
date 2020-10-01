/*******************************************************************************
 * ALMA - Atacama Large Millimeter Array
 * Copyright (c) ESO - European Southern Observatory, 2011
 * (in the framework of the ALMA collaboration).
 * All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 *******************************************************************************/
package alma.obops.aqua.qa0;
/**
 * Copyright European Southern Observatory 2010
 */


import java.util.HashMap;
import java.util.Map;

/**
 * GUI constants
 *
 * @author fjulbe, Feb 24, 2010
 * @author rtobar, Jun 28th, 2012
 */

public abstract class AquaGUIConstants {
	
	/**
	 * The name of this application. This is used to create consistently named
	 * loggers, and for named singletons when needed.
	 */
	public static final String AQUA_APP_NAME = "webAqua";

	/**
	 * Name of the parameter that contains the location of the spring
	 * XML file that contains the beans used by the system
	 */
	public static final String SPRING_DAM_CONFIG_LOCATION = "springConfig";

	/**
	 * Name of the parameter that indicates whether the ACS listener
	 * should be started with the application or not.
	 */
	public static final String START_ONLINE_SYSTEM = "initOnlineSystem";

	/**
	 * Path where the servlet that serves AQUA attachments can be found
	 */
	public static final String ATTACHMENT_SERVLET = "AquaAttachmentRetrieverServlet";

	/**
	 * Path where the servlet that generate QA reports can be found
	 */
	public static final String REPORT_SERVLET = "AquaReportCreatorServlet";

	public static final String RESOURCES_PATH = "../../resources/";

	
	/* ============== */
	// Event queue names
	/* ============== */
	
	/**
	 * The name of Manual Calibration comment events queue.
	 */
	public static final String AQUA_MAN_CAL_QUEUE = "manCalQueue";
	
	/**
	 * The name of Manual Imaging attachment events queue.
	 */
	public static final String AQUA_MAN_IMG_QUEUE = "manImgQueue";

	/**
	 * Queue for user session log
	 *
	 */
	public static final String AQUA_UPDATE_USER_LOG_QUEUE = "updateUserLogQueue";


	/* ============== */
	// Event names
	/* ============== */

	/**
	 * Event sent by any part of the application when an AQUA entity has been
	 * selected, and is supposed to be opened on its editor
	 */
	public static final String ON_ENTITY_SELECTED = "onEntitySelected";

	/**
	 * Event sent by any part of the application when an AQUA needs to refresh entity
	 */
	public static final String ON_ENTITY_REFRESH = "onEntityRefresh";

	/**
	 * Event sent by the main AQUA QA page, informing subscribers that the list
	 * of unflagged objects has changed.
	 */
	public static final String ON_UNFLAGGED_OBJECTS_UPDATED = "onUnflaggedObjectsUpdated";

	/**
	 * Event sent when clicking one of the baseline listbox cells of the phase summary QA0 report,
	 * indicating that a baseline has been selected
	 */
	public static final String ON_BASELINE_SELECTED = "onBaselineSelected";

	/**
	 * Event sent when clicking one of the trash icons of the attachments table, indicating that
	 * the corresponding attachment should be removed
	 */
	public static final String ON_ATTACHMENT_REMOVED = "onAttachmentRemoved";

	/**
	 * Event sent when clicking one of the trash icons of the comments table, indicating that
	 * the corresponding comment should be removed
	 */
	public static final String ON_COMMENT_REMOVED = "onCommentRemoved";

	/**
	 * Event sent after a user clicks on the "Save" button of the popup window that
	 * appears when clicking on one of the edit icons of the comments table, indicating that
	 * the corresponding comment is to be edited.
	 */
	public static final String ON_COMMENT_EDITED = "onCommentEdited";

	/**
	 * Event sent when a new QA flag is set into in one of the QA flagging windows
	 */
	public static final String ON_QAFLAG_SET = "onQAFlagSet";

	/**
	 * Event sent when a advanced QA@ search triggered
	 */
	public static final String ON_QA2_ADVANCED_SEARCH_TRIGGERED = "onQA2Search";


	/**
	 * Event queue name to update PL Flags
	 */
	public static final String UPDATE_FLAGS = "updateFlags";

	/**
	 * Event sent when a PL flag is created
	 */
	public static final String ON_PLFLAG_READY = "onPlFlagReady";

	/**
	 * MVVM global event sent when a PL flag is created to trigger global command
	 */
	public static final String PLFLAG_READY = "plFlagReady";

	public static final String RECIPE_CHANGE_WARNING = "You should enter recipe change reason(s)";

	/**
	 * Event sent when a advanced QA@ search triggered
	 */
	public static final String AQUA_LOG_HELPER_ATTRIBUTE = "aquaUserLogHelper";

	// RelativePointing field names for the two options
	public static final String RELATIVE_POITING_FIELDNAME_COLLOFFSET = "collOffsetRelative";
	public static final String RELATIVE_POITING_FIELDNAME_BEAMWIDTH = "beamwidth";
	
	// Combobox labels
	public static final String[] SIDEBAND_RATIO_PLOTS_LABEL = {
							"aqua.query.sidebandratio.plot.time",
							"aqua.query.sidebandratio.plot.frequency",
							"aqua.query.sidebandratio.plot.tsys"};
	
	public static final String[] PHASE_FLUCTUATION_PLOTS_LABEL = {
							"aqua.query.phase.plot.phaserms"
							};

	// X-AXIS PARAMS
	public static final String TEMPERATURE = "aqua.weather.temperature";
	public static final String PRESSURE = "aqua.weather.pressure";
	public static final String WIND_SPEED = "aqua.weather.wind";
	public static final String WIND_DIRECTION = "aqua.weather.winddirection";
	public static final String HUMIDITY = "aqua.weather.humidity";
	public static final String FREQUENCY = "aqua.axis.frequency";
	public static final String TIME = "aqua.axis.time";
	public static final String BASELINELENGTH_TOTAL = "aqua.axis.baseline.total";
	public static final String BASELINELENGTH_SINGLE = "aqua.axis.baseline.specific";
	public static final String PWV = "aqua.axis.PWV";
	public static final String TSYS = "aqua.axis.tsys";
	public static final String AZIMUTH = "aqua.axis.azimuth";
	public static final String AZIMUTH_VS_ELEVATION = "aqua.query.azimuthelevation.cross.plot";
	public static final String ELEVATION = "aqua.axis.elevation";
	
	public static final String XYZ_ELEVATION = "aqua.focus.query.xyz.elevation";
	public static final String CROSS_PLOTS = "aqua.focus.query.crossplots";
	public static final String VS_RELATIVEPOINTING = "aqua.focus.query.pointing";
	public static final String VS_ANTENNA_GAIN = "aqua.focus.query.antennagain";

	private static final String RMS_TIME = "aqua.query.bandpass.rms.time.plot";
	private static final String RMS_FREQUENCY = "aqua.query.bandpass.rms.frequency.plot";
	private static final String CURVE_FREQUENCY = "aqua.query.bandpass.curve.time.plot";
	
	public static final String[] PHASE_PLOT_TYPES = {
														TIME,
														TEMPERATURE,
														PRESSURE,
														WIND_SPEED,			
														HUMIDITY,
														BASELINELENGTH_TOTAL,
														BASELINELENGTH_SINGLE														
													};
	
	public static final String[] FULL_WEATHER_PARAMS_SET = {TEMPERATURE,
															PRESSURE,
															WIND_SPEED,			
															WIND_DIRECTION,
															HUMIDITY
															};
	
	public static final String[] SKY_OPACITY_WEATHER_PARAMS_SET = 
															{TEMPERATURE,
															PRESSURE,
															WIND_SPEED,			
															HUMIDITY
															};

	public static final String[] BANDPASS_PARAMS_SET = 
													{	RMS_TIME,
														RMS_FREQUENCY,
														CURVE_FREQUENCY
													};
	
	public static final String[] FOCUS_PARAMS_SET = 
													{
													TIME,
													XYZ_ELEVATION,
													CROSS_PLOTS,			
													VS_RELATIVEPOINTING,
													VS_ANTENNA_GAIN,
													TEMPERATURE
													};
	
	
	public static final String[] DELAY_PARAMS_SET = 
													{TIME,
													AZIMUTH,			
													ELEVATION,
													BASELINELENGTH_TOTAL,
													BASELINELENGTH_SINGLE
													};	

	public static final String[] SYS_TEMP_PARAMS_SET = 
														{
														TIME,
														TEMPERATURE,
														PRESSURE,
														WIND_SPEED,			
														HUMIDITY
														};	
	
	public static final String[] RECEIVER_TEMP_PARAMS_SET = 
														{
														FREQUENCY,
														TSYS
														};		
	
	// ANTENNA GAIN AXIS SET
	public static final String[] GENERIC_PLOT_TYPES = {
														TIME,
														FREQUENCY,
														TEMPERATURE,
														PRESSURE,
														WIND_SPEED,			
														WIND_DIRECTION,
														HUMIDITY
													};

	public static final String[] RELATIVE_POINTING_PLOT_TYPES = {
														TIME,
														FREQUENCY,														
														TEMPERATURE,
														PRESSURE,
														WIND_SPEED,			
														WIND_DIRECTION,
														HUMIDITY,
														AZIMUTH_VS_ELEVATION
													};
	
	public static final String[] FITTING_FUNCTION_TYPES = {
																"Polynomial"
															};
	
	public static final String URL_PLOT_DESC = "url";
	public static final String DIV_ID = "divId";
	public static final String PLOT_TITLE = "plotTitle";
	public static final String XAXIS = "xAxis";
	public static final String YAXIS = "yAxis";
	
	public static final String CORRECTED = "corrected"; 
	public static final String UNCORRECTED = "uncorrected";

	public static final String FITTED_CURVE_LEGEND = "aqua.fit.curve.legend";
	
	public static final String[] POLYNOMIAL_GRADE = {
														"1","2","3","4","5","6",
														"7","8","10","11","12", 
														"13", "14", "15"
													};	
	
	public static final String ANTENNA_MAKER_LABEL = "aqua.antenna.maker";

    /**
     * Map of the weather params label to be displayed in the plot
     */
    protected static final Map<String, String> xAxisLabels;

    static {
    	xAxisLabels = new HashMap<String,String>();
    	xAxisLabels.put( "Time",  "Date" );       	
    	xAxisLabels.put( "Frequency",  "Frequency (GHz)" );    	
    	xAxisLabels.put( "Temperature",  "Ambient Temperature (K)" );
    	xAxisLabels.put( "Pressure", 	"Pressure (HPa)" );
    	xAxisLabels.put( "Wind speed",   "Windspeed (m/sec)" );
    	xAxisLabels.put( "Wind Direction",	"WindDirection (rad)" );
    	xAxisLabels.put( "Humidity",     "PWC (mm)" );
    	xAxisLabels.put( "XYZ vs. elevation", "Elevation (Rad)" );
    	xAxisLabels.put( "vs. Antenna Gain", "Antenna Gain" );
    	xAxisLabels.put( "vs. Relative pointing", "Relative Pointing" );
    	xAxisLabels.put( "X-Y-Z cross plots", "Offset" );
    	xAxisLabels.put( "TSys", "System Temperature (K)" );
    	xAxisLabels.put( "Projected distance", "Baseline length (m)" );
    	xAxisLabels.put( "Specific Baseline", "Baseline length (m)" );
    	xAxisLabels.put( "Azimuth", "Azimuth (rad)" );
    	xAxisLabels.put( "Elevation", "Elevation (rad)" );
    	xAxisLabels.put( "Azimuth vs. Elevation", "Elevation (rad)" ); 
    	
    }

    
    
    /**
     * Returns the full label for the xAxis
     * @param key
     * @return
     */
    public static String getXAxisLabels(String key) {
    	return xAxisLabels.get(key);
    }
	
}
