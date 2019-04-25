package knf.kuma.home

import knf.kuma.database.CacheDB
import knf.kuma.search.SearchAdvObject
import org.jetbrains.anko.doAsync

object StaffRecommendations {
    private val recommendations: List<String> = listOf(
            "2928", "2597", "1279", "1615", "363", "1706", "2950", "1182", "2479", "2478", "29", "30", "854",
            "2508", "2702", "1976", "2526", "5", "1290", "2852", "937", "3043", "2091", "1165", "3105", "2104",
            "108", "117", "996", "92", "1739", "1740", "181", "2671", "727", "1632", "2687", "3106", "1487",
            "1488", "1019", "460", "1493", "1494", "918", "1294", "2485", "2833", "2340", "3033", "1008", "869",
            "1286", "1791", "1044", "801", "901", "1048", "2279", "292", "28", "284", "2305", "1899", "2834",
            "349", "1627", "312", "277", "1497", "711", "150", "2748", "2901", "2639", "2860", "638", "773",
            "2959", "263", "1618", "1897", "2290", "125", "1324", "2533", "2636", "2789", "2395", "1028", "904",
            "721", "1753", "2794", "1813", "3104", "1912", "853", "661", "840", "2983", "310", "151", "863",
            "2382", "2599", "992", "126", "609", "127", "787", "880", "128", "704", "1272", "31", "1347", "129",
            "1554", "1555", "419", "1082", "2094", "1320", "1634", "1629", "8", "878", "1635", "2454", "1213",
            "2229", "1638", "1443", "2150", "1486", "465", "640", "966", "967", "1095", "1474", "2659", "2126",
            "978", "13", "2602", "2848", "1325", "364", "556", "1064", "1132", "557", "1322", "1930", "189",
            "1209", "1427", "1001", "1681", "1617", "353", "953", "951", "952", "80", "860", "3001", "1222",
            "897", "1195", "1092", "1179", "1091", "133", "417", "2954", "134", "1429", "42", "1196", "589",
            "366", "1958", "862", "2660", "2802", "2874", "2984", "1909", "2846", "1135", "1132", "9", "1139",
            "60", "593", "2661", "2652", "1237", "1915", "1297", "2287", "2767", "2111", "2840", "1316", "6",
            "2100", "1576", "866", "849", "91", "719", "1031", "1870", "2947", "2998", "778", "2762", "1900",
            "93", "2245", "2247", "174", "2491", "2657", "338", "986", "2295", "2233", "1428", "26", "27",
            "1318", "2903", "49", "1620", "2536", "2895", "3065", "393", "40", "39", "10", "2588", "1127",
            "118", "119", "299", "267", "268", "1780", "3113", "104", "639", "872", "1628", "1608", "1218",
            "34", "2496", "1793", "2457", "2876", "1002", "2089", "226", "1208", "690", "2740", "2543", "2446",
            "2586", "3137", "3011", "135", "136", "2110"
    )

    fun createList(onCreate: (list: List<SearchAdvObject>) -> Unit) {
        doAsync {
            onCreate(CacheDB.INSTANCE.animeDAO().animesWithIDRandom(recommendations))
        }
    }
}