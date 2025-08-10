package com.huysama.builderDto.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.huysama.builderDto.HamaClass.Base64DecodedMultipartFile;
import io.micrometer.common.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class HamaUtil {
    public static String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    private static Gson gson;
    static {
        getGson();
    }

    public static <T> String stringify(T t) {
        return gson.toJson(t);
    }

    public static <T> T parse(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T, H> H toHis(T t, Class<H> clazz) {
        return gson.fromJson(gson.toJson(t), clazz);
    }

    public static <T> T parse(String json, Type typeToken) {
        return (T) gson.fromJson(json, typeToken);
    }

    public static <T> List<List<T>> partition(List<T> listIN, int size) {
        List<List<T>> partitionedList = new ArrayList<>();
        for (int i = 0; i < listIN.size(); i += size) {
            partitionedList.add(listIN.subList(i, Math.min(i + size, listIN.size())));
        }
        return partitionedList;
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .serializeNulls().setDateFormat(FORMAT_DATETIME)
                    .registerTypeAdapter(Integer.class, (JsonDeserializer<Integer>) (json, type, context) -> {
                        String value = json.getAsString();
                        if (value.isEmpty()) return null;
                        return new BigDecimal(value).intValueExact();
                    })
                    .registerTypeAdapter(Double.class, (JsonDeserializer<Double>) (json, type, context) -> {
                        String value = json.getAsString();
                        return value.isEmpty() ? null : Double.parseDouble(value);
                    })
                    .registerTypeAdapter(BigDecimal.class, (JsonDeserializer<BigDecimal>) (json, type, context) -> {
                        String value = json.getAsString();
                        return value.isEmpty() ? null : new BigDecimal(value);
                    })
                    .registerTypeAdapter(Long.class, (JsonDeserializer<Long>) (json, type, context) -> {
                        String value = json.getAsString();
                        if (value.isEmpty()) return null;
                        return new BigDecimal(value).longValueExact();
                    })
                    .create();
        }
        return gson;
    }

    public static <T> List<List<T>> partition(List<T> listIN) {
        return partition(listIN, 999);
    }

    public static String nvl(String input, String vcl) {
        if (!hasText(input)) {
            return vcl;
        }
        return input;
    }

    public static Date getDateWithoutTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDateWithoutTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static String formatDate(Date date, String format) {
        if (date == null) return null;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

//    public static String formatDate(Date date) {
//        return formatDate(date, AppConstant.FORMAT_DATETIME);
//    }

    public static Date plusDate(Date openDate, int freqUnit) {
        long time = openDate.getTime();
        return run(time, freqUnit);
    }

    public static java.sql.Date run(long time, Integer days) {
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(new java.sql.Date(time));
        cal1.add(Calendar.DATE, days);
        return new java.sql.Date(cal1.getTime().getTime());
    }

    public static <T, T2> T2 cloneObj(T obj1, Class<T2> t2Class) {
        return parse(stringify(obj1), t2Class);
    }


    public static <T> T nvl(T input, T vcl) {
        return Optional.ofNullable(input).orElse(vcl);
    }

    public static <T> Optional<T> nvl(T input) {
        return Optional.ofNullable(input);
    }

//    public static StringBuilder readConfigJson(String pathAndFileName) {
//        try {
//            ClassPathResource resource = new ClassPathResource("config/" + pathAndFileName);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
//            StringBuilder jsonContent = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                jsonContent.append(line);
//            }
//            reader.close();
//            return jsonContent;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new StringBuilder("{}");
//        }
//    }

    public static String generateStrongGUID() {
        StringBuilder tmpBuffer = new StringBuilder(8);
        String counterData = get62CounterVariation();
        tmpBuffer.append(padHex(counterData, 3) + counterData);
        String hexTimeNow = Long.toHexString(System.currentTimeMillis());
        StringBuilder guid = new StringBuilder(16);
        guid.append(86);
        guid.append(hexTimeNow);
        guid.append(tmpBuffer.toString());

        if (guid.length() > 16) {
            return guid.substring(0, 16);
        }
        return guid.toString();
    }

    private static int counter = 0;

    private static String get62CounterVariation() {
        StringBuilder hexVal = new StringBuilder();
        counter += 1;
        if (counter >= 238328) {
            counter = 1;
        }
        counter %= 238328;
        int decValue = counter;
        while (decValue > 0) {
            int digit = decValue % 62;
            hexVal.append("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
                    .charAt(digit));
            decValue /= 62;
        }
        return hexVal.reverse().toString();
    }

    private static String padHex(String string, int times) {
        StringBuilder tmpBuffer = new StringBuilder();
        if (string.length() < times) {
            for (int count = 0; count < times - string.length(); count++) {
                tmpBuffer.append('0');
            }
        }
        return tmpBuffer.toString();
    }

    public static void main(String[] args) {
        try {
//            KeyPair keyPair = RSACommon.generateRSAKeyPair();
//            System.out.println("PRIVATE");
//            System.out.println(keyPair.getPrivate().toString());
//            System.out.println("PUBLIC");
//            System.out.println(keyPair.getPublic().toString());
//            RSACommon.getPublicKeyFromPrivateKey();


//            String json = "{ \"pernetweight\": \"0.1\" }";
//            Gson gson = getGson();
//
//            Tb_Whm_Pallet_Prd_Dtl test = gson.fromJson(json, Tb_Whm_Pallet_Prd_Dtl.class);
//            System.out.println(test);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Date toDate(String date) {
        return toDate(date, FORMAT_DATETIME);
    }

    public static Date toDate(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> List<T> cloneList(List<T> list, Class<T> clazz) {
        return list.stream().map(item -> cloneObj(item, clazz)).toList();
    }

    public static boolean isEmp(String str) {
        return !hasText(str);
    }

    public static String priceWithoutDecimal(BigDecimal price, String currency) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        if (!currency.equals("VND")) {
            formatter = new DecimalFormat("###,###,###.##");
        }
        return formatter.format(price);
    }

//    public static String getStringWithDefault(String text1, String text2) {
//        return StringUtils.hasText(text1) ? text1 : text2;
//    }

//    public static <T> T getObjectWithDefault(T obj1, T obj2) {
//        return (obj1 != null) ? obj1 : obj2;
//    }
//
//    public static BigDecimal scaleAmount(BigDecimal amount) {
//        if (amount == null) {
//            return null;
//        }
//        return amount.setScale(AppConstant.SCALE_AMOUNT, RoundingMode.HALF_UP);
//    }
//
//    public static <T> List<T> cloneListV2(List<T> source, Class<T> classT) {
////        return (List<T>) gson.fromJson(gson.toJson(source), new TypeToken<List<classT>>(){}.getType());
//        Type listType = com.nimbusds.jose.shaded.gson.reflect.TypeToken.getParameterized(List.class, classT).getType();
//        return new ArrayList<>(gson.fromJson(gson.toJson(source), listType));
//    }

    public static BigDecimal calcBigDecimal(BigDecimal value, Long quantity) {
        if (value == null) return null;
        return value.multiply(BigDecimal.valueOf(quantity));
    }

    public static BigDecimal sumBigDecimal(BigDecimal valueA, BigDecimal valueB) {
        if (valueA == null && valueB == null) return null;
        if (valueA == null) return valueB;
        if (valueB == null) return valueA;
        return valueA.add(valueB);
    }

    public static String removeVNAccent(String str, boolean notSpace) {
        if (str == null) return "";
        str = str.trim();
        str = str.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        str = str.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        str = str.replaceAll("[ìíịỉĩ]", "i");
        str = str.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        str = str.replaceAll("[ùúụủũưừứựửữ]", "u");
        str = str.replaceAll("[ỳýỵỷỹ]", "y");
        str = str.replaceAll("đ", "d");
        str = str.replaceAll("[ÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴ]", "A");
        str = str.replaceAll("[ÈÉẸẺẼÊỀẾỆỂỄ]", "E");
        str = str.replaceAll("[ÌÍỊỈĨ]", "I");
        str = str.replaceAll("[ÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠ]", "O");
        str = str.replaceAll("[ÙÚỤỦŨƯỪỨỰỬỮ]", "U");
        str = str.replaceAll("[ỲÝỴỶỸ]", "Y");
        str = str.replaceAll("Đ", "D");
//        str = str.replaceAll("[^\\w\\s]", ""); // Remove non-word, non-space characters
        if (notSpace) {
            str = str.replace(" ", "");
        }

        return str;
    }

    public static String removeVNAccent(String str) {
        return removeVNAccent(str, false);
    }

    public static <T> void cleanEmptyStrings(T obj) {
        if (obj == null) return;
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == String.class) {
                field.setAccessible(true); // cho phép truy cập private field
                try {
                    String value = (String) field.get(obj);
                    if (value != null && value.trim().isEmpty()) {
                        field.set(obj, null);
                    }
                } catch (IllegalAccessException e) {
                    // xử lý nếu có lỗi khi truy cập field
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isNumberInCSV(String csv, Long num) {
        if (csv == null || csv.isEmpty()) return false;

        String[] items = csv.split(",");
        for (String item : items) {
            if (item.trim().equals(String.valueOf(num))) {
                return true;
            }
        }
        return false;
    }

    public static Date mergeDateAndTime(Date inputDate, Date inputDateTime) {
        if (inputDate == null) return null;
        if (inputDateTime == null) return inputDate;
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(inputDate);
        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(inputDateTime);
        // Gộp ngày từ inputDate và giờ từ inputDateTime
        dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
        dateCal.set(Calendar.MILLISECOND, timeCal.get(Calendar.MILLISECOND));
        return dateCal.getTime();
    }

    public static boolean isBigDecimalNegative(BigDecimal check) {
        return check != null && check.compareTo(BigDecimal.ZERO) < 0;
    }


    public static String getContentTypeFromFileName(String fileName) {
        String contentType = "application/octet-stream";
        System.out.println("upload file: " + fileName);
        try {
            Path path = Paths.get(fileName);
            if (hasText(Files.probeContentType(path))) {
                contentType = Files.probeContentType(path);
            } else {
                contentType = getMimeTypeFromExtension(fileName);
            }
        } catch (Exception e) {
            contentType = "application/octet-stream"; // Giá trị mặc định nếu không xác định được
        }
        return contentType;
    }

    public static String getMimeTypeFromExtension(String filename) {
        if (filename.endsWith(".txt")) return "text/plain";
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return "image/jpeg";
        if (filename.endsWith(".png")) return "image/png";
        if (filename.endsWith(".pdf")) return "application/pdf";
        if (filename.endsWith(".xlsx")) return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        return "application/octet-stream"; // Mặc định
    }

    public static String getExtensionFromMime(String base64) {
        if (base64.startsWith("data:")) {
            int start = base64.indexOf("/") + 1;
            int end = base64.indexOf(";");
            if (start > 0 && end > start) {
                return "." + base64.substring(start, end);
            }
        }
        return "";
    }

    public static boolean isBlank(String stg) {
        return !hasText(nvl(stg, "").trim());
    }

    public static MultipartFile convertBase64ToMultipart(String base64, String fileName) {
        String[] parts = base64.split(",");
        String base64Data = parts.length > 1 ? parts[1] : parts[0];

        byte[] fileContent = Base64.getDecoder().decode(base64Data);
        MultipartFile file = new Base64DecodedMultipartFile(fileContent, fileName, getMimeTypeFromExtension(fileName));

        return file;
    }

    public static boolean hasText(@Nullable CharSequence str) {
        if (str == null) {
            return false;
        } else {
            int strLen = str.length();
            if (strLen == 0) {
                return false;
            } else {
                for(int i = 0; i < strLen; ++i) {
                    if (!Character.isWhitespace(str.charAt(i))) {
                        return true;
                    }
                }

                return false;
            }
        }
    }

    public static boolean hasText(@Nullable String str) {
        return str != null && !str.isBlank();
    }
}
