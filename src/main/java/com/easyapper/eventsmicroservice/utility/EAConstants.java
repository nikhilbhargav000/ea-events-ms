package com.easyapper.eventsmicroservice.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.easyapper.eventsmicroservice.entity.AddressSubEntity;
import com.easyapper.eventsmicroservice.entity.EventBookingSubEntity;
import com.easyapper.eventsmicroservice.entity.PostedEventEntity;
import com.easyapper.eventsmicroservice.entity.LocationSubEntity;
import com.easyapper.eventsmicroservice.model.AddressDTO;
import com.easyapper.eventsmicroservice.model.EventBookingDTO;
import com.easyapper.eventsmicroservice.model.EventDTO;
import com.easyapper.eventsmicroservice.model.LocationDTO;

public final class EAConstants {
	
	public static final String EVENT_COLLECTION_PREFIX = "Event_";
	public static final String EVENT_COLLECTION_POSTFIX = "_Collection";

	public static final String POSTED_EVENT_ID_PREFIX = "PTD_";
	public static final String SUBSCRIBED_EVENT_ID_PREFIX = "SUB_";

	public static final String SUBSCRIBED_EVENT_COLLECTION_DB_NAME = "SubscribedEventCollection";
	public static final String EVENT_TYPE_POSTED = "posted";
	public static final String EVENT_TYPE_SUBSCRIBED = "subscribed";

	public static final String EVENT_TYPE_KEY = "event_type";
	public static final String EVENT_CATEGORY_KEY = "event_category";
	public static final String EVENT_SUB_CATEGORY_KEY = "event_subcategory";
	public static final String EVENT_LOCATION_KEY = "event_location";
	public static final String EVENT_LOCATION_LONGITUDE_KEY = "longitude";
	public static final String EVENT_LOCATION_LATITUDE_KEY = "latitude";
	public static final String EVENT_ADDRESS_KEY = "address";
	public static final String EVENT_ADDRESS_ID_KEY = "id";
	public static final String EVENT_ADDRESS_CITY_KEY = "city";
	public static final String EVENT_ADDRESS_STREET_KEY = "street";
	public static final String EVENT_ADDRESS_PIN_KEY = "pin";
	public static final String EVENT_ORGANIZER_EMAIL_KEY = "organizer_email";
	public static final String EVENT_NAME_KEY = "event_name";
	public static final String EVENT_DESCRIBTION_KEY = "event_description";
	public static final String EVENT_IMAGE_URL_KEY = "event_image_url";
	public static final String EVENT_START_DATE_KEY = "event_start_date";
	public static final String EVENT_LAST_DATE_KEY = "event_last_date";
	public static final String EVENT_MIN_AGE_KEY = "event_min_age";
	public static final String EVENT_MAX_AGE_KEY = "event_max_age";
	public static final String EVENT_PRICE_KEY = "event_price";
	public static final String EVENT_BOOKING_KEY = "event_booking";
	public static final String EVENT_BOOKING_URL_KEY = "url";
	public static final String EVENT_BOOKING_INQUIRY_URL_KEY = "inquiry_url";
	public static final String EVENT_POSTED_EVENT_ID_KEY = "posted_event_id";
	public static final String EVENT_START_TIME_KEY = "event_start_time";
	public static final String EVENT_END_TIME_KEY = "event_end_time";

	public static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy";
	public static final String TIME_FORMAT_PATTERN = "HH:mm";
	
}
