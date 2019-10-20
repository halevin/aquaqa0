package alma.obops.aqua.qa0.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author amchavan, 25 Aug 2015
 */

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such entity")  // 404
public class EntityNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -8942790832729107125L;
}
