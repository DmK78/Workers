package ru.job4j.workers;

public class DbSchema {
    public static final class SpecialitiesTable {
        public static final String NAME = "specialities";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                SpecialitiesTable.NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Cols.NAME + " TEXT " + ")";


        public static final class Cols {
            public static final String NAME = "name";
        }
    }

    public static final class WorkersTable {
        public static final String NAME = "workers";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                WorkersTable.NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Cols.FIRST_NAME + " TEXT, " +
                Cols.LAST_NAME + " TEXT, " +
                Cols.BIRTH_DATE + " TEXT, " +
                Cols.PHOTO + " INTEGER, " +
                Cols.SPECIALITY_ID + " INTEGER" + ")";

        public static final class Cols {
            public static final String FIRST_NAME = "first_name";
            public static final String LAST_NAME = "last_name";
            public static final String BIRTH_DATE = "birth_date";
            public static final String PHOTO = "photo";
            public static final String SPECIALITY_ID = "speciality_id";
        }
    }

}