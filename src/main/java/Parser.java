    import org.jsoup.Jsoup;
    import org.jsoup.nodes.Document;
    import org.jsoup.nodes.Element;
    import org.jsoup.select.Elements;

    import java.io.IOException;
    import java.net.URL;
    import java.util.regex.Matcher;
    import java.util.regex.Pattern;

    public class Parser {

        private static Document getPage() throws IOException {
            String url = "https://pogoda.spb.ru/";
            Document page = Jsoup.parse( new URL(url), 3000);
            return page;
        }

        //20.05 Четверг погода сегодня
        //20.05
        // \d{2}\.\d{2}
        private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

        private static String getDateFromString(String stringDate) throws Exception {
            Matcher matcher = pattern.matcher(stringDate);
            if (matcher.find()) {
                return matcher.group();
            }
            throw new Exception("Cant extract date from string");
        }

        private static int printPartValues(Elements values, int index) {
            int iterationCount = 4;

            if (index == 0){
                Element valueLn = values.get(3);
                boolean isMorning = valueLn.text().contains("Утро");
                boolean isAfternoon = valueLn.text().contains("День");

                if (isMorning) {
                    iterationCount = 3;
                }
                if (isAfternoon) {
                    iterationCount = 2;
                }

            }


            for (int i = 0; i<iterationCount; i++){
                Element valueLine = values.get(index + i);
                for (Element td : valueLine.select("td")) {
                    System.out.print(td.text() +"    ");
                }
                System.out.println();
            }

            return iterationCount;
        }

        public static void main(String[] args) throws Exception {
            Document page = getPage();
            //css query language
            Element tableWhr  = page.select("table[class=wt]").first();
            Elements names = tableWhr.select("tr[class=wth]");
            Elements values = tableWhr.select("tr[valign=top]");

            int index = 0;


            for (Element name : names) {
                String dateString = name.select("th[id=dt]").text();
                String date = getDateFromString(dateString);

                System.out.println(date + "    Явления      Температура   Давление   Влажность   Ветер   ");
               int iterationCount =  printPartValues(values, index);
               index = index + iterationCount;
            }



        }
    }
