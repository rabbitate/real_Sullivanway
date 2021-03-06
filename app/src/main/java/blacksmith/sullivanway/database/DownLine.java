package blacksmith.sullivanway.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DownLine {
    private static SQLiteDatabase db;
    public static String TB_NAME = "DownLine";
    static String SQL_CREATE = String.format("CREATE TABLE %s(" +
            "lineNm TEXT NOT NULL," +
            "startStnNm TEXT NOT NULL," +
            "endStnNm TEXT NOT NULL," +
            "primary key(lineNm, startStnNm, endStnNm));", TB_NAME);
    static String SQL_DROP = String.format("DROP TABLE IF EXISTS %s", TB_NAME);
    static String SQL_DELETE_ALL = String.format("DELETE FROM %s", TB_NAME);

    static boolean isUpward(String lineNm, String startStnNm, String endStnNm) {
        String sql = String.format("SELECT startStnNm FROM %s WHERE lineNm='%s' AND startStnNm='%s' AND endStnNm='%s'",
                TB_NAME, lineNm, startStnNm, endStnNm);
        Cursor cursor = db.rawQuery(sql, null);
        boolean isUpward = !cursor.moveToNext();
        cursor.close();
        return isUpward;
    }

    public static String getNextStnNms(String lineNm, String stnNm, int toward) {
        StringBuilder nextStnNms = new StringBuilder();

        String sql = (toward == 1) ?
                String.format("SELECT startStnNm FROM %s WHERE lineNM='%s' AND endStnNm='%s'",
                        TB_NAME, lineNm, stnNm) :
                String.format("SELECT endStnNm FROM %s WHERE lineNM='%s' AND startStnNm='%s'",
                        TB_NAME, lineNm, stnNm);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext())
            nextStnNms.append(cursor.getString(0));
        while (cursor.moveToNext())
            nextStnNms.append(", ").append(cursor.getString(0));
        cursor.close();
        return nextStnNms.toString();
    }


    /* Database */
    private static void insert(String lineNm, String startStnNm, String endStnNm) {
        String sql = String.format(
                "INSERT INTO %s VALUES('%s','%s','%s');",
                TB_NAME, lineNm, startStnNm, endStnNm);
        db.execSQL(sql);
    }

    static void setDatabase(SQLiteDatabase db) {
        DownLine.db = db;
    }

    static void initDatabase() {
        // ?????? -> ??????
        // 1??????
        insert("1??????", "?????????", "?????????");
        insert("1??????", "?????????", "??????");
        insert("1??????", "??????", "???????????????");
        insert("1??????", "???????????????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "?????????");
        insert("1??????", "?????????", "??????");
        insert("1??????", "??????", "?????????");
        insert("1??????", "?????????", "?????????");
        insert("1??????", "?????????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "?????????");
        insert("1??????", "?????????", "??????");
        insert("1??????", "??????", "?????????");
        insert("1??????", "?????????", "?????????");
        insert("1??????", "?????????", "??????");
        insert("1??????", "??????", "?????????");
        insert("1??????", "?????????", "?????????");
        insert("1??????", "?????????", "?????????");
        insert("1??????", "?????????", "?????????");
        insert("1??????", "?????????", "?????????");
        insert("1??????", "?????????", "??????5???");
        insert("1??????", "??????5???", "??????3???");
        insert("1??????", "??????3???", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "?????????");
        insert("1??????", "?????????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "?????????");
        insert("1??????", "?????????", "?????????");
        insert("1??????", "?????????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "?????????");
        insert("1??????", "?????????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "?????????");
        insert("1??????", "?????????", "??????");
        insert("1??????", "??????", "?????????");
        insert("1??????", "?????????", "??????");
        insert("1??????", "??????", "?????????????????????");
        insert("1??????", "?????????????????????", "??????");
        insert("1??????", "??????", "????????????");
        insert("1??????", "????????????", "??????");
        insert("1??????", "????????????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "????????????");
        insert("1??????", "????????????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "?????????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "?????????");
        insert("1??????", "?????????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "?????????");
        insert("1??????", "?????????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "??????(????????????)");
        insert("1??????", "??????(????????????)", "??????");
        insert("1??????", "??????", "??????");
        insert("1??????", "??????", "????????????");
        insert("1??????", "????????????", "??????(????????????)");

        // 2??????
        insert("2??????", "??????", "???????????????");
        insert("2??????", "???????????????", "?????????3???");
        insert("2??????", "?????????3???", "?????????4???");
        insert("2??????", "?????????4???", "???????????????????????????");
        insert("2??????", "???????????????????????????", "??????");
        insert("2??????", "??????", "????????????");
        insert("2??????", "????????????", "?????????");
        insert("2??????", "?????????", "?????????");
        insert("2??????", "?????????", "??????");
        insert("2??????", "??????", "??????");
        insert("2??????", "??????", "??????");
        insert("2??????", "??????", "??????");
        insert("2??????", "??????", "??????");
        insert("2??????", "??????", "?????????");
        insert("2??????", "??????", "????????????");
        insert("2??????", "????????????", "??????");
        insert("2??????", "??????", "??????");
        insert("2??????", "??????", "????????????");
        insert("2??????", "????????????", "??????");
        insert("2??????", "??????", "????????????");
        insert("2??????", "????????????", "???????????????");
        insert("2??????", "???????????????", "??????");
        insert("2??????", "??????", "??????");
        insert("2??????", "??????", "??????");
        insert("2??????", "??????", "??????");
        insert("2??????", "??????", "??????");
        insert("2??????", "??????", "??????");
        insert("2??????", "??????", "??????");
        insert("2??????", "??????", "??????");
        insert("2??????", "??????", "?????????");
        insert("2??????", "?????????", "???????????????");
        insert("2??????", "???????????????", "??????");
        insert("2??????", "??????", "??????");
        insert("2??????", "??????", "?????????");
        insert("2??????", "?????????", "?????????????????????");
        insert("2??????", "?????????????????????", "??????");
        insert("2??????", "??????", "?????????");
        insert("2??????", "?????????", "???????????????");
        insert("2??????", "???????????????", "????????????");
        insert("2??????", "????????????", "?????????");
        insert("2??????", "?????????", "?????????");
        insert("2??????", "?????????", "??????");
        insert("2??????", "??????", "???????????????");
        insert("2??????", "???????????????", "??????");
        insert("2??????", "??????", "??????");
        insert("2??????", "??????", "????????????");
        insert("2??????", "????????????", "??????");
        insert("2??????", "??????", "??????");
        insert("2??????", "??????", "??????");
        insert("2??????", "??????", "?????????");
        insert("2??????", "?????????", "??????");

        // 3??????
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "?????????");
        insert("3??????", "?????????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "?????????");
        insert("3??????", "?????????", "?????????");
        insert("3??????", "?????????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "?????????");
        insert("3??????", "?????????", "?????????");
        insert("3??????", "?????????", "?????????");
        insert("3??????", "?????????", "??????");
        insert("3??????", "??????", "??????3???");
        insert("3??????", "??????3???", "?????????3???");
        insert("3??????", "?????????3???", "?????????");
        insert("3??????", "?????????", "????????????");
        insert("3??????", "????????????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "?????????");
        insert("3??????", "?????????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "???????????????");
        insert("3??????", "???????????????", "??????");
        insert("3??????", "??????", "???????????????");
        insert("3??????", "???????????????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "?????????");
        insert("3??????", "?????????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "??????");
        insert("3??????", "??????", "????????????");
        insert("3??????", "????????????", "????????????");
        insert("3??????", "????????????", "??????");

        // 4??????
        insert("4??????", "?????????", "??????");
        insert("4??????", "??????", "??????");
        insert("4??????", "??????", "??????");
        insert("4??????", "??????", "??????");
        insert("4??????", "??????", "??????(????????????)");
        insert("4??????", "??????(????????????)", "??????");
        insert("4??????", "??????", "???????????????");
        insert("4??????", "???????????????", "??????");
        insert("4??????", "??????", "??????????????????");
        insert("4??????", "??????????????????", "???????????????");
        insert("4??????", "???????????????", "??????");
        insert("4??????", "??????", "?????????");
        insert("4??????", "?????????", "???????????????????????????");
        insert("4??????", "???????????????????????????", "?????????");
        insert("4??????", "?????????", "??????");
        insert("4??????", "??????", "??????");
        insert("4??????", "??????", "??????");
        insert("4??????", "??????", "????????????");
        insert("4??????", "????????????", "?????????");
        insert("4??????", "?????????", "?????????");
        insert("4??????", "?????????", "??????");
        insert("4??????", "??????", "??????");
        insert("4??????", "??????", "???????????????(??????)");
        insert("4??????", "???????????????(??????)", "??????");
        insert("4??????", "??????", "?????????");
        insert("4??????", "?????????", "?????????");
        insert("4??????", "?????????", "????????????");
        insert("4??????", "????????????", "?????????");
        insert("4??????", "?????????", "??????");
        insert("4??????", "??????", "??????????????????");
        insert("4??????", "??????????????????", "?????????");
        insert("4??????", "?????????", "??????");
        insert("4??????", "??????", "??????");
        insert("4??????", "??????", "??????");
        insert("4??????", "??????", "??????");
        insert("4??????", "??????", "?????????");
        insert("4??????", "?????????", "?????????");
        insert("4??????", "?????????", "??????");
        insert("4??????", "??????", "?????????");
        insert("4??????", "?????????", "?????????");
        insert("4??????", "?????????", "??????");
        insert("4??????", "??????", "??????");
        insert("4??????", "??????", "??????");
        insert("4??????", "??????", "??????");
        insert("4??????", "??????", "????????????");
        insert("4??????", "????????????", "??????");
        insert("4??????", "??????", "?????????");

        // 5??????
        insert("5??????", "??????", "?????????");
        insert("5??????", "?????????", "????????????");
        insert("5??????", "????????????", "??????");
        insert("5??????", "??????", "??????");
        insert("5??????", "??????", "??????");
        insert("5??????", "??????", "?????????");
        insert("5??????", "?????????", "??????");
        insert("5??????", "??????", "?????????");
        insert("5??????", "?????????", "??????");
        insert("5??????", "??????", "??????");
        insert("5??????", "??????", "?????????");
        insert("5??????", "?????????", "??????");
        insert("5??????", "??????", "???????????????");
        insert("5??????", "???????????????", "???????????????");
        insert("5??????", "???????????????", "??????");
        insert("5??????", "??????", "?????????");
        insert("5??????", "?????????", "????????????");
        insert("5??????", "????????????", "??????");
        insert("5??????", "??????", "??????");
        insert("5??????", "??????", "?????????");
        insert("5??????", "?????????", "?????????");
        insert("5??????", "?????????", "?????????");
        insert("5??????", "?????????", "?????????");
        insert("5??????", "?????????", "??????3???");
        insert("5??????", "??????3???", "?????????4???");
        insert("5??????", "?????????4???", "???????????????????????????");
        insert("5??????", "???????????????????????????", "??????");
        insert("5??????", "??????", "?????????");
        insert("5??????", "?????????", "??????");
        insert("5??????", "??????", "?????????");
        insert("5??????", "?????????", "??????");
        insert("5??????", "??????", "?????????");
        insert("5??????", "?????????", "?????????");
        insert("5??????", "?????????", "??????");
        insert("5??????", "??????", "?????????");
        insert("5??????", "?????????", "?????????");
        insert("5??????", "?????????", "??????");
        insert("5??????", "??????", "??????");
        insert("5??????", "??????", "??????");
        insert("5??????", "??????", "????????????");
        insert("5??????", "????????????", "??????");
        insert("5??????", "??????", "??????");
        insert("5??????", "??????", "?????????");
        insert("5??????", "??????", "?????????");
        insert("5??????", "?????????", "???????????????");
        insert("5??????", "???????????????", "??????");
        insert("5??????", "??????", "??????");
        insert("5??????", "??????", "??????");
        insert("5??????", "??????", "??????");
        insert("5??????", "??????", "??????");

        // 6??????
        insert("6??????", "??????", "??????");
        insert("6??????", "??????", "??????");
        insert("6??????", "??????", "?????????");
        insert("6??????", "?????????", "?????????");
        insert("6??????", "?????????", "??????");
        insert("6??????", "??????", "??????");
        insert("6??????", "??????", "??????");
        insert("6??????", "??????", "??????");
        insert("6??????", "??????", "????????????????????????");
        insert("6??????", "????????????????????????", "??????????????????");
        insert("6??????", "??????????????????", "????????????");
        insert("6??????", "????????????", "??????");
        insert("6??????", "??????", "??????");
        insert("6??????", "??????", "??????");
        insert("6??????", "??????", "?????????");
        insert("6??????", "?????????", "??????");
        insert("6??????", "??????", "??????");
        insert("6??????", "??????", "???????????????");
        insert("6??????", "???????????????", "?????????");
        insert("6??????", "?????????", "?????????(????????????)");
        insert("6??????", "?????????(????????????)", "?????????");
        insert("6??????", "?????????", "?????????");
        insert("6??????", "?????????", "????????????");
        insert("6??????", "????????????", "??????");
        insert("6??????", "??????", "??????");
        insert("6??????", "??????", "??????");
        insert("6??????", "??????", "?????????");
        insert("6??????", "?????????", "??????");
        insert("6??????", "??????", "??????");
        insert("6??????", "??????", "??????");
        insert("6??????", "??????", "?????????");
        insert("6??????", "?????????", "??????");
        insert("6??????", "??????", "?????????");
        insert("6??????", "?????????", "?????????");
        insert("6??????", "?????????", "??????");
        insert("6??????", "??????", "????????????");
        insert("6??????", "????????????", "?????????");
        insert("6??????", "?????????", "?????????(???????????????)");

        // 7??????
        insert("7??????", "??????", "?????????");
        insert("7??????", "?????????", "?????????");
        insert("7??????", "?????????", "??????");
        insert("7??????", "??????", "??????");
        insert("7??????", "??????", "??????");
        insert("7??????", "??????", "??????");
        insert("7??????", "??????", "??????");
        insert("7??????", "??????", "????????????");
        insert("7??????", "????????????", "??????");
        insert("7??????", "??????", "??????");
        insert("7??????", "??????", "??????");
        insert("7??????", "??????", "??????");
        insert("7??????", "??????", "?????????");
        insert("7??????", "?????????", "?????????");
        insert("7??????", "?????????", "??????");
        insert("7??????", "??????", "??????");
        insert("7??????", "??????", "??????????????????");
        insert("7??????", "??????????????????", "????????????");
        insert("7??????", "????????????", "???????????????");
        insert("7??????", "???????????????", "??????");
        insert("7??????", "??????", "????????????");
        insert("7??????", "????????????", "??????");
        insert("7??????", "??????", "??????");
        insert("7??????", "??????", "??????");
        insert("7??????", "??????", "???????????????");
        insert("7??????", "???????????????", "??????");
        insert("7??????", "??????", "???????????????(??????)");
        insert("7??????", "???????????????(??????)", "??????");
        insert("7??????", "??????", "???????????????");
        insert("7??????", "???????????????", "??????");
        insert("7??????", "??????", "????????????");
        insert("7??????", "????????????", "??????????????????");
        insert("7??????", "??????????????????", "?????????");
        insert("7??????", "?????????", "??????");
        insert("7??????", "??????", "??????");
        insert("7??????", "??????", "?????????");
        insert("7??????", "?????????", "?????????????????????");
        insert("7??????", "?????????????????????", "??????");
        insert("7??????", "??????", "???????????????");
        insert("7??????", "???????????????", "??????");
        insert("7??????", "??????", "??????");
        insert("7??????", "??????", "?????????");
        insert("7??????", "?????????", "?????????????????????");
        insert("7??????", "?????????????????????", "??????");
        insert("7??????", "??????", "?????????");
        insert("7??????", "?????????", "????????????");
        insert("7??????", "????????????", "??????");
        insert("7??????", "??????", "???????????????");
        insert("7??????", "???????????????", "?????????");
        insert("7??????", "?????????", "????????????");

        // 8??????
        insert("8??????", "??????", "??????");
        insert("8??????", "??????", "????????????");
        insert("8??????", "????????????", "????????????");
        insert("8??????", "????????????", "??????");
        insert("8??????", "??????", "??????");
        insert("8??????", "??????", "??????");
        insert("8??????", "??????", "????????????");
        insert("8??????", "????????????", "??????");
        insert("8??????", "??????", "??????");
        insert("8??????", "??????", "??????");
        insert("8??????", "??????", "??????");
        insert("8??????", "??????", "??????????????????");
        insert("8??????", "??????????????????", "???????????????");
        insert("8??????", "???????????????", "??????");
        insert("8??????", "??????", "??????");
        insert("8??????", "??????", "??????");

        // 9??????
        insert("9??????", "??????", "????????????");
        insert("9??????", "????????????", "????????????");
        insert("9??????", "????????????", "?????????");
        insert("9??????", "?????????", "????????????");
        insert("9??????", "????????????", "????????????");
        insert("9??????", "????????????", "??????");
        insert("9??????", "??????", "??????");
        insert("9??????", "??????", "??????");
        insert("9??????", "??????", "??????");
        insert("9??????", "??????", "?????????");
        insert("9??????", "?????????", "?????????");
        insert("9??????", "?????????", "??????");
        insert("9??????", "??????", "???????????????");
        insert("9??????", "???????????????", "?????????");
        insert("9??????", "?????????", "??????");
        insert("9??????", "??????", "?????????");
        insert("9??????", "?????????", "??????");
        insert("9??????", "??????", "??????");
        insert("9??????", "??????", "??????");
        insert("9??????", "??????", "?????????");
        insert("9??????", "?????????", "?????????");
        insert("9??????", "?????????", "???????????????");
        insert("9??????", "???????????????", "??????");
        insert("9??????", "??????", "?????????");
        insert("9??????", "?????????", "??????");
        insert("9??????", "??????", "?????????");
        insert("9??????", "?????????", "????????????");
        insert("9??????", "????????????", "?????????");
        insert("9??????", "?????????", "???????????????");

        // ????????????????????????
        insert("????????????????????????", "??????", "??????");
        insert("????????????????????????", "??????", "????????????");
        insert("????????????????????????", "????????????", "????????????????????????");
        insert("????????????????????????", "????????????????????????", "????????????");
        insert("????????????????????????", "????????????", "??????");
        insert("????????????????????????", "??????", "??????");
        insert("????????????????????????", "??????", "??????????????????");
        insert("????????????????????????", "??????????????????", "??????");
        insert("????????????????????????", "??????", "??????");
        insert("????????????????????????", "??????", "??????????????????");
        insert("????????????????????????", "??????????????????", "??????????????????");

        // ??????????????????
        insert("??????????????????", "??????????????????", "???????????????");
        insert("??????????????????", "???????????????", "????????????");
        insert("??????????????????", "????????????", "?????????????????????");
        insert("??????????????????", "?????????????????????", "????????????");
        insert("??????????????????", "????????????", "??????");

        // ?????????
        insert("?????????", "?????????", "?????????");
        insert("?????????", "?????????", "??????????????????");
        insert("?????????", "??????????????????", "????????????");
        insert("?????????", "????????????", "?????????");
        insert("?????????", "?????????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "?????????");
        insert("?????????", "?????????", "???????????????");
        insert("?????????", "???????????????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "?????????");
        insert("?????????", "?????????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "????????????");
        insert("?????????", "????????????", "????????????");
        insert("?????????", "????????????", "??????");
        insert("?????????", "??????", "??????");

        // ????????????
        insert("????????????", "??????", "?????????");
        insert("????????????", "?????????", "??????");
        insert("????????????", "??????", "??????");
        insert("????????????", "??????", "??????");
        insert("????????????", "??????", "??????");
        insert("????????????", "??????", "??????");
        insert("????????????", "??????", "?????????????????");
        insert("????????????", "?????????????????", "?????????");
        insert("????????????", "?????????", "?????????");
        insert("????????????", "?????????", "????????????????????");
        insert("????????????", "????????????????????", "??????");
        insert("????????????", "??????", "??????");
        insert("????????????", "??????", "??????");
        insert("????????????", "??????", "????????????????????");

        // ?????????
        insert("?????????", "?????????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "?????????");
        insert("?????????", "?????????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "????????????");
        insert("?????????", "????????????", "?????????");
        insert("?????????", "?????????", "??????");
        insert("?????????", "??????", "?????????");
        insert("?????????", "?????????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "?????????");
        insert("?????????", "?????????", "?????????");
        insert("?????????", "?????????", "??????");
        insert("?????????", "??????", "?????????");
        insert("?????????", "?????????", "?????????");
        insert("?????????", "?????????", "??????");

        // ??????1??????
        insert("??????1??????", "??????", "??????");
        insert("??????1??????", "??????", "??????");
        insert("??????1??????", "??????", "??????");
        insert("??????1??????", "??????", "??????");
        insert("??????1??????", "??????", "??????????????????");
        insert("??????1??????", "??????????????????", "??????");
        insert("??????1??????", "??????", "??????");
        insert("??????1??????", "??????", "????????????");
        insert("??????1??????", "????????????", "????????????");
        insert("??????1??????", "????????????", "??????");
        insert("??????1??????", "??????", "??????");
        insert("??????1??????", "??????", "???????????????");
        insert("??????1??????", "???????????????", "???????????????");
        insert("??????1??????", "???????????????", "????????????");
        insert("??????1??????", "????????????", "????????????");
        insert("??????1??????", "????????????", "???????????????");
        insert("??????1??????", "???????????????", "???????????????");
        insert("??????1??????", "???????????????", "??????");
        insert("??????1??????", "??????", "?????????");
        insert("??????1??????", "?????????", "?????????");
        insert("??????1??????", "?????????", "??????");
        insert("??????1??????", "??????", "??????");
        insert("??????1??????", "??????", "???????????????");
        insert("??????1??????", "???????????????", "???????????????");
        insert("??????1??????", "???????????????", "??????????????????");
        insert("??????1??????", "??????????????????", "???????????????");
        insert("??????1??????", "???????????????", "???????????????");
        insert("??????1??????", "???????????????", "??????????????????");

        // ??????2??????
        insert("??????2??????", "????????????", "??????");
        insert("??????2??????", "??????", "???????????????");
        insert("??????2??????", "???????????????", "??????");
        insert("??????2??????", "??????", "??????");
        insert("??????2??????", "??????", "??????");
        insert("??????2??????", "??????", "??????");
        insert("??????2??????", "??????", "?????????");
        insert("??????2??????", "?????????", "?????????????????????");
        insert("??????2??????", "?????????????????????", "?????????");
        insert("??????2??????", "?????????", "??????");
        insert("??????2??????", "??????", "??????????????????");
        insert("??????2??????", "??????????????????", "??????");
        insert("??????2??????", "??????", "??????????????????");
        insert("??????2??????", "??????????????????", "????????????");
        insert("??????2??????", "????????????", "?????????");
        insert("??????2??????", "?????????", "??????????????????");
        insert("??????2??????", "??????????????????", "??????");
        insert("??????2??????", "??????", "????????????");
        insert("??????2??????", "????????????", "???????????????");
        insert("??????2??????", "???????????????", "????????????");
        insert("??????2??????", "????????????", "???????????????");
        insert("??????2??????", "???????????????", "???????????????");
        insert("??????2??????", "???????????????", "??????");
        insert("??????2??????", "??????", "????????????");
        insert("??????2??????", "????????????", "???????????????");
        insert("??????2??????", "???????????????", "??????");

        // ???????????????
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "????????????????????????");
        insert("???????????????", "????????????????????????", "??????");
        insert("???????????????", "??????", "??????(???????????????)");
        insert("???????????????", "??????(???????????????)", "??????");
        insert("???????????????", "??????", "????????????");
        insert("???????????????", "????????????", "?????????");
        insert("???????????????", "?????????", "??????");
        insert("???????????????", "??????", "???????????????");
        insert("???????????????", "???????????????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "?????????");
        insert("???????????????", "?????????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "?????????");
        insert("???????????????", "?????????", "?????????");
        insert("???????????????", "?????????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "?????????");
        insert("???????????????", "?????????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "??????");

        // ?????????
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "????????????");
        insert("?????????", "????????????", "??????");
        insert("?????????", "??????", "?????????");
        insert("?????????", "?????????", "???????????????");
        insert("?????????", "???????????????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "???????????????");
        insert("?????????", "???????????????", "??????");

        // ????????????
        insert("????????????", "??????", "??????");
        insert("????????????", "??????", "??????????????????");
        insert("????????????", "??????????????????", "???????????????");
        insert("????????????", "???????????????", "??????");
        insert("????????????", "??????", "??????");
        insert("????????????", "??????", "??????");
        insert("????????????", "??????", "????????????");
        insert("????????????", "????????????", "??????");
        insert("????????????", "??????", "??????");
        insert("????????????", "??????", "????????????");
        insert("????????????", "????????????", "??????");

        // ?????????
        insert("?????????", "?????????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "????????????");
        insert("?????????", "????????????", "????????????");
        insert("?????????", "????????????", "?????????");
        insert("?????????", "?????????", "?????????????????????");
        insert("?????????", "?????????????????????", "?????????");
        insert("?????????", "?????????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "?????????");
        insert("?????????", "?????????", "??????");
        insert("?????????", "??????", "??????");
        insert("?????????", "??????", "??????");

        // ??????????????????
        insert("??????????????????", "??????", "??????");
        insert("??????????????????", "??????", "??????");
        insert("??????????????????", "??????", "??????????????????");
        insert("??????????????????", "??????????????????", "???????????????");
        insert("??????????????????", "???????????????", "??????");
        insert("??????????????????", "??????", "???????????????");
        insert("??????????????????", "???????????????", "??????");
        insert("??????????????????", "??????", "??????");
        insert("??????????????????", "??????", "????????????????????????");
        insert("??????????????????", "????????????????????????", "??????");
        insert("??????????????????", "??????", "??????");
        insert("??????????????????", "??????", "??????");
        insert("??????????????????", "??????", "??????");
        insert("??????????????????", "??????", "??????");

        // ???????????????
        insert("???????????????", "???????????????", "????????????");
        insert("???????????????", "????????????", "4.19????????????");
        insert("???????????????", "4.19????????????", "?????????");
        insert("???????????????", "?????????", "??????");
        insert("???????????????", "??????", "??????");
        insert("???????????????", "??????", "???????????????");
        insert("???????????????", "???????????????", "??????");
        insert("???????????????", "??????", "??????????????????");
        insert("???????????????", "??????????????????", "??????");
        insert("???????????????", "??????", "??????????????????");
        insert("???????????????", "??????????????????", "??????");
        insert("???????????????", "??????", "?????????");

    }
    
}
