package com.easyapper.eventsmicroservice.utility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.multipart.MultipartFile;

import com.easyapper.eventsmicroservice.entity.AddressSubEntity;
import com.easyapper.eventsmicroservice.entity.EventBookingSubEntity;
import com.easyapper.eventsmicroservice.entity.LocationSubEntity;
import com.easyapper.eventsmicroservice.entity.PostedEventEntity;
import com.easyapper.eventsmicroservice.entity.SubscribedEventEntity;
import com.easyapper.eventsmicroservice.exception.NoExtensionFoundException;
import com.easyapper.eventsmicroservice.model.AddressDto;
import com.easyapper.eventsmicroservice.model.EventBookingDto;
import com.easyapper.eventsmicroservice.model.LocationDto;
import com.easyapper.eventsmicroservice.model.EventDto;

public class EAUtil {
	
	/**
	 * Logger
	 */
	private static EALogger logger = EALogger.getLogger();
	
	public static String getEventCollectionName(String userId) {
		return EAConstants.EVENT_COLLECTION_PREFIX + userId + EAConstants.EVENT_COLLECTION_POSTFIX;
	}

	public static String getPostedEventId(String userId, Long nextSeq) {
		return EAConstants.POSTED_EVENT_ID_PREFIX + userId + "_" + String.valueOf(nextSeq);
	}
	public static String getSubscribedEventId( Long nextSeq) {
		return EAConstants.SUBSCRIBED_EVENT_ID_PREFIX + String.valueOf(nextSeq);
	}

	public static boolean canBePostedEventId(String eventId) {
		if(eventId.startsWith(EAConstants.POSTED_EVENT_ID_PREFIX)) {
			return true;
		}
		return false;
	}
	public static boolean canBeSubscribedEventId(String eventId) {
		if(eventId.startsWith(EAConstants.SUBSCRIBED_EVENT_ID_PREFIX)) {
			return true;
		}
		return false;
	}
	
	public static String getUserId(String postedEventId) {
		String[] arrStr = postedEventId.split("_");
		if(arrStr.length >= 2) {
			return arrStr[1];
		}else {
			return null;
		}
		
	}
	
	public static String getHashString_ForPostedEvent(EventDto eventDto) {
		StringBuilder stringBuilder = new StringBuilder();
		if(eventDto.getEvent_category() != null)
			stringBuilder.append(eventDto.getEvent_category());
		if(eventDto.getEvent_subcategory() != null)
			stringBuilder.append(eventDto.getEvent_subcategory());
		if(eventDto.getEvent_name() != null)
			stringBuilder.append(eventDto.getEvent_name());
		if(eventDto.getEvent_description() != null)
			stringBuilder.append(eventDto.getEvent_description());
		if(eventDto.getEvent_location().getAddress().getCity() != null)
			stringBuilder.append(eventDto.getEvent_location().getAddress().getCity());
		if(eventDto.getEvent_location().getAddress().getStreet() != null)
			stringBuilder.append(eventDto.getEvent_location().getAddress().getStreet());
		if(eventDto.getEvent_start_date() != null)
			stringBuilder.append(eventDto.getEvent_start_date());
		if(eventDto.getEvent_last_date() != null)
			stringBuilder.append(eventDto.getEvent_last_date());
		return stringBuilder.toString();
	}
	
	public static String getTimeStampFormatStr(Timestamp timeStamp) {
		SimpleDateFormat formatter = new SimpleDateFormat(EAConstants.TIME_STAMP_FORMAT_PATTERN);
		return formatter.format(timeStamp);
	}
	public static String getDateFormatStr(Date dateObj) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(EAConstants.DATE_FORMAT_PATTERN);
		return dateFormat.format(dateObj);
	}
	public static String getTimeFormatStr(Date dateObj) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(EAConstants.TIME_FORMAT_PATTERN);
		return dateFormat.format(dateObj);
	}

	public static Date getDateFormatObj(String strDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(EAConstants.DATE_FORMAT_PATTERN);
		try {
			return dateFormat.parse(strDate);
		} catch (ParseException e) {
			logger.warning(e.getMessage(), e);
			return null;
		}
	}
	public static Date getTimeFormatObj(String strTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(EAConstants.TIME_FORMAT_PATTERN);
		try {
			return dateFormat.parse(strTime);
		} catch (ParseException e) {
			logger.warning(e.getMessage(), e);
			return null;
		}
	}
	
	public static String getImageRootDir() {
		String rootDir = System.getProperty("catalina.home");
		String imageDir = rootDir + EAConstants.IMAGE_BASE_DIRECTORY;
		return imageDir;
	}
	
	public static String getImageFileExtension(MultipartFile imageFile) throws NoExtensionFoundException {
		String arrStr[] = imageFile.getOriginalFilename().split(EAConstants.DOT_REGEX);
		if(arrStr.length >= 2) {
			return EAConstants.DOT + arrStr[arrStr.length - 1];
		}else {
			throw new NoExtensionFoundException();
		}
	}
	
	public static String getDomainUrl(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":"
				+request.getServerPort();
	}
}
