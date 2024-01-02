package lunatic.athenarpg.dialogue;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NPCDialogue {

    private static final List<String> GREETINGS = Arrays.asList(
            "Halo! Bagaimana harimu? Sepertinya aku membutuhkan bantuanmu.",
            "Selamat datang, petualang! Aku punya tugas untukmu.",
            "Ah, kau datang! Aku memerlukan bantuan dengan sesuatu.",
            "Hai! Siap untuk petualangan baru?",
            "Selamat datang, pemberani! Sepertinya ada pekerjaan untukmu.",
            "Halo, petualang! Aku punya misi menarik untukmu.",
            "Terima kasih sudah datang! Aku punya tugas yang menantang.",
            "Apa kabar? Aku memerlukan bantuanmu untuk tugas spesial.",
            "Hei, pahlawan! Kabar baik? Aku punya tugas seru buatmu.",
            "Selamat datang! Ada petualangan menarik yang menunggu.",
            "Halo! Aku butuh bantuanmu untuk misi rahasia.",
            "Terima kasih sudah datang! Ayo kita mulai petualangan baru."
    );

    private static final List<String> QUEST_INTRO = Arrays.asList(
            "Hari ini aku ingin berbicara tentang %s.",
            "Aku memiliki sesuatu yang menarik untuk dibagikan mengenai %s.",
            "Dengarkan ceritaku tentang %s.",
            "Apa pendapatmu tentang %s.",
            "Aku ingin berbicara sedikit tentang %s.",
            "Bagaimana kalau kita membahas tentang %s?",
            "Aku ingin tahu apa yang kamu pikirkan tentang %s.",
            "Ada hal menarik yang ingin aku sampaikan tentang %s.",
            "Apa reaksimu jika kita membicarakan tentang %s?",
            "Jangan lewatkan informasi menarik ini tentang %s.",
            "Aku ingin berbagi cerita menarik tentang %s.",
            "Bagaimana jika kita bahas sedikit tentang %s."
    );

    private static final List<String> QUEST_ITEM_NEEDED = Arrays.asList(
            "Aku membutuhkan %s untuk proyekku saat ini.",
            "Bawakan aku %s, dan kamu akan mendapatkan imbalan.",
            "Untuk quest ini, aku mencari %s.",
            "Kamu dapat membantu dengan mendapatkan %s untukku.",
            "Aku membutuhkan %s untuk proyekku sekarang.",
            "Bawa %s padaku, dan kamu akan mendapatkan imbalan yang besar.",
            "Dalam quest ini, aku mencari %s untuk diselesaikan.",
            "Kamu bisa membantu dengan mendapatkan %s untukku.",
            "Untuk proyekku, aku membutuhkan %s sekarang.",
            "Bawakan aku %s, dan aku akan memberikan hadiah istimewa.",
            "Dalam quest ini, aku mencari %s untuk diselesaikan.",
            "Kamu bisa membantu dengan mendapatkan %s untukku."
    );

    private static final List<String> QUEST_COMPLETED = Arrays.asList(
            "Luangkan waktu untuk merayakan kesuksesanmu! Selamat, petualang!",
            "Aksi luar biasa! Kamu benar-benar pahlawan di mataku.",
            "Quest selesai dengan gemilang! Teruskan perjalananmu yang menakjubkan.",
            "Kemampuanmu luar biasa! Terimakasih atas dedikasimu dalam menyelesaikan quest ini.",
            "Aku tidak bisa lebih bahagia! Kamu luar biasa!",
            "Tak terduga! Kamu berhasil menyelesaikan quest dengan gemilang.",
            "Kamu adalah harapan kami! Teruslah melangkah maju, pahlawan!",
            "Inilah bukti keberanianmu! Aku bangga bisa bekerja denganmu.",
            "Hanya sedikit yang bisa seperti kamu! Quest selesai dengan baik.",
            "Perjalananmu penuh warna! Terimakasih atas kerja kerasmu dalam menyelesaikan quest."
    );

    private static final Random random = new Random();

    public static String getGreeting() {
        return getRandomElement(GREETINGS);
    }

    public static String getQuestIntro(String questTitle) {
        String introTemplate = getRandomElement(QUEST_INTRO);

        List<String> staffPrefixes = Arrays.asList("[Clairmmont] ");
        List<String> prefixes = Arrays.asList("[Bryzle] ");
        List<String> difficulties = Arrays.asList("[EASY] ", "[MEDIUM] ", "[HARD] ", "[EXPERT] ");

        String staffPrefix = getMatchingPrefix(questTitle, staffPrefixes);

        // Replace specific prefixes with empty strings
        for (String staffPrefixx : staffPrefixes) {
            questTitle = questTitle.replace(staffPrefixx, "");
        }

        for (String prefix : prefixes) {
            questTitle = questTitle.replace(prefix, "");
        }

        for (String difficulty : difficulties) {
            questTitle = questTitle.replace(difficulty, "");
        }

        return String.format(introTemplate, "§b" + staffPrefix + "§c" + questTitle + "§f");
    }

    private static String getMatchingPrefix(String questTitle, List<String> prefixes) {
        for (String staffPrefix : prefixes) {
            if (questTitle.contains(staffPrefix.trim())) {
                return staffPrefix;
            }
        }
        return "";
    }



    public static String getQuestItemNeeded(Map<Material, Integer> items) {
        StringBuilder itemNeededMessage = new StringBuilder("");

        int itemCount = 0;
        for (Map.Entry<Material, Integer> entry : items.entrySet()) {
            if (itemCount > 0) {
                itemNeededMessage.append("§f, ");
            }

            Material material = entry.getKey();
            int amount = entry.getValue();
            String itemName = formatItemName(material); // Use the custom format method
            itemNeededMessage.append(itemName).append(" §ex").append(amount).append("§f");

            itemCount++;
        }

        String itemTemplate = getRandomElement(QUEST_ITEM_NEEDED);
        return String.format(itemTemplate, itemNeededMessage.toString());
    }
    public static String getQuestCompletedMessage() {
        return getRandomElement(QUEST_COMPLETED);
    }

    private static String formatItemName(Material material) {
        // Customize this method based on your desired format
        String itemName = material.toString().toLowerCase().replace("_", " ");
        itemName = itemName.substring(0, 1).toUpperCase() + itemName.substring(1).toLowerCase();
        return "§d" + itemName;
    }
    private static String getRandomElement(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }
}
