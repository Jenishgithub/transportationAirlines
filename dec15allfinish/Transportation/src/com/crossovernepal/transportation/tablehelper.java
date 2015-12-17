package com.crossovernepal.transportation;

import android.provider.BaseColumns;

public class tablehelper {

	public static final class TransportationColumns implements BaseColumns {
		// This class cannot be instantiated
		private TransportationColumns() {
		}

		public static final String TABLENAME = "transportation";

		public static final String ID = TABLENAME + ".id";
		public static final String ROUTE_ID = TABLENAME + ".route_id";
		public static final String TYPE_ID = TABLENAME + ".type_id";
		public static final String COMPANY_ID = TABLENAME + ".company_id";
		public static final String CATEGORY_ID = TABLENAME + ".category_id";
		public static final String CONTACT_NAME = TABLENAME + ".contact_name";
		public static final String CONTACT_PHONE = TABLENAME + ".contact_phone";
		public static final String DEPART_TIME = TABLENAME + ".depart_time";
		public static final String ARRIVAL_TIME = TABLENAME + ".arrival_time";
		public static final String INTERVAL = TABLENAME + ".interval";
		public static final String RETURN_TYPE = TABLENAME + ".return_type";
		public static final String COST = TABLENAME + ".cost";
		public static final String REMARKS = TABLENAME + ".remarks";
	}

	public static final class RouteColumns implements BaseColumns {
		// This class cannot be instantiated
		private RouteColumns() {
		}

		public static final String TABLENAME = "route";

		public static final String ID = TABLENAME + ".id";
		public static final String NAME = TABLENAME + ".name";
		public static final String FULL_ROUTE = TABLENAME + ".full_route";
	}

	public static final class CompanyColumns implements BaseColumns {
		// This class cannot be instantiated
		private CompanyColumns() {
		}

		public static final String TABLENAME = "company";

		public static final String ID = TABLENAME + ".id";
		public static final String NAME = TABLENAME + ".name";
		public static final String ALIAS = TABLENAME + ".alias";
		public static final String ADDRESS = TABLENAME + ".address";
		public static final String PHONE = TABLENAME + ".phone";
	}

	public static final class ScheduleColumns implements BaseColumns {
		// This class cannot be instantiated
		private ScheduleColumns() {
		}

		public static final String TABLENAME = "schedule";

		public static final String ID = TABLENAME + ".id";
		public static final String TRANSPORTATION_ID = TABLENAME
				+ ".transportation_id";
		public static final String SCHEDULE_TYPE = TABLENAME + ".schedule_type";

	}

	public static final class BookmarksColumns implements BaseColumns {
		// This class cannot be instantiated
		private BookmarksColumns() {
		}

		public static final String TABLENAME = "bookmarks";
		public static final String TRANSPORTATION_ID = TABLENAME
				+ ".transportation_id";
		public static final String ID = TABLENAME + ".id";
		public static final String ROUTE_NODE_ID = TABLENAME + ".route_node_id";

	}

	public static final class CategoryColumns implements BaseColumns {
		private CategoryColumns() {
		}

		public static final String TABLENAME = "category";
		public static final String ID = TABLENAME + ".id";
		public static final String NAME = TABLENAME + ".name";
	}

	public static final class DistrictColumns implements BaseColumns {
		private DistrictColumns() {
		}

		public static final String TABLENAME = "district";
		public static final String ID = TABLENAME + ".id";
		public static final String NAME = TABLENAME + ".name";

	}

	public static final class LanguageColumns implements BaseColumns {
		private LanguageColumns() {
		}

		public static final String TABLENAME = "language";
		public static final String ID = TABLENAME + ".id";
		public static final String NAME = TABLENAME + ".name";
	}

	public static final class NodeColumns implements BaseColumns {
		private NodeColumns() {
		}

		public static final String TABLENAME = "node";
		public static final String ID = TABLENAME + ".id";
		public static final String NAME = TABLENAME + ".name";
		public static final String LAT = TABLENAME + ".lat";
		public static final String LNG = TABLENAME + ".lon";
		public static final String SPECIALITIES = TABLENAME + ".specialities";
		public static final String DISTRICT_ID = TABLENAME + ".district_id";

	}

	public static final class Route_NodeColumns implements BaseColumns {
		private Route_NodeColumns() {
		}

		public static final String TABLENAME = "route_node";
		public static final String ID = TABLENAME + ".id";
		public static final String ROUTE_ID = TABLENAME + ".route_id";
		public static final String NODE_ID = TABLENAME + ".node_id";
		public static final String ROUTE_COUNT = TABLENAME + ".route_count";

	}

	public static final class ServiceColumns implements BaseColumns {
		private ServiceColumns() {
		}

		public static final String TABLENAME = "service";
		public static final String ID = TABLENAME + ".id";
		public static final String NAME = TABLENAME + ".name";

	}

	public static final class TypeColumns implements BaseColumns {
		private TypeColumns() {
		}

		public static final String TABLENAME = "type";
		public static final String ID = TABLENAME + ".id";
		public static final String NAME = TABLENAME + ".name";
	}

	public static final class TransportationDetailsColumns implements
			BaseColumns {
		private TransportationDetailsColumns() {
		}

		public static final String TABLENAME = "transportation_details";
		public static final String ID = TABLENAME + ".id";
		public static final String TRANSPORTATION_ID = TABLENAME
				+ ".transportation_id";
		public static final String NODE_ID = TABLENAME + ".node_id";
		public static final String ARRIVAL_TIME = TABLENAME + ".arrival_time";
		public static final String DEPART_TIME = TABLENAME + ".depart_time";
		public static final String DETAILS = TABLENAME + ".details";
	}

	public static final class TransportationAirlineDetailsColumns implements
			BaseColumns {
		private TransportationAirlineDetailsColumns() {
		}

		public static final String TABLENAME = "transportation_airline_details";
		public static final String ID = TABLENAME + ".id";
		public static final String TRANSPORTATION_ID = TABLENAME
				+ ".transportation_id";
		public static final String AIRLINE_ID = TABLENAME + ".airline_id";
		public static final String AIRLINE_CLASS_ID = TABLENAME
				+ ".airline_class_id";

	}

	public static final class AirlinesClassesColumns implements BaseColumns {
		private AirlinesClassesColumns() {
		}

		public static final String TABLENAME = "airlines_classes";
		public static final String ID = TABLENAME + ".id";
		public static final String AIRLINE_ID = TABLENAME + ".airline_id";
		public static final String NAME = TABLENAME + ".name";
	}

	public static final class AirlinesColumns implements BaseColumns {
		private AirlinesColumns() {
		}

		public static final String TABLENAME = "airlines";
		public static final String ID = TABLENAME + ".id";
		public static final String NAME = TABLENAME + ".name";
	}

}
