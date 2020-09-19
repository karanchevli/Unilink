import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Unilink {

    static String userName;

    static Scanner sc = new Scanner(System.in);
    static List<Event> pst = new ArrayList<Event>();

    static int eventCount = 0;

    public static void main(String[] args) {

        int choice = 0;
        int sChoice = 0;

        while(choice != 2) {
            System.out.print("**UniLink System**\n1. Log in\n2. Quit\n");
            System.out.print("Enter your choice:");
            choice = sc.nextInt();
            sc.nextLine();


            if (choice > 2) {
                System.out.println("Input should be in Range of [1-2]");
            }
            else if (choice == 1) {
                System.out.print("Enter username:");
                userName = sc.nextLine();

                System.out.println("\nWelcome "+ userName+"!");

                while(sChoice != 9) {
                    System.out.println("\n** Student Menu **\n1. New Event\n"
                            + "2. Reply To Post\n3. Display My Posts\n4. Display All Posts\n5. Close Post\n"
                            + "6. Delete Post\n7. Log Out");

                    if(sChoice > 9) {
                        System.out.println("Input should be in Range of [1-9]");
                    }

                    System.out.print("Enter your choice:");
                    sChoice = sc.nextInt();
                    sc.nextLine();

                    switch(sChoice) {

                        case 1:
                            newEvent();
                            break;
                        case 2:
                            replyToPost();
                            break;
                        case 3:
                            displayUserPost();
                            break;
                        case 4:
                            displayAllPost();
                            break;
                        case 5:
                            closePost();
                            break;
                        case 6:
                            deletePost();
                            break;
                    }
                }
            }
            sChoice = 0;
        }

    }

    private static void newEvent() {
        String eveName = null;
        String eveDesc = null;
        String eveVenue = null;
        String eveDate = "1111/1111/1111";
        int eveCapacity = 0;

        System.out.println("Enter details for event below");
        while(isNullOrEmpty(eveName)) {
            System.out.print("Name:");
            eveName = sc.nextLine();
        }
        while(isNullOrEmpty(eveDesc)) {
            System.out.print("Description:");
            eveDesc = sc.nextLine();
        }
        while(isNullOrEmpty(eveVenue)) {
            System.out.print("Venue:");
            eveVenue = sc.nextLine();
        }
        while(!validateDate(eveDate)) {
            System.out.print("Date(MM/DD/YYYY):");
            eveDate = sc.nextLine();
        }
        while(eveCapacity < 1 ) {
            System.out.print("Capacity:");
            eveCapacity = sc.nextInt();
            sc.nextLine();
        }

        String eveId = generateId();

        Event e = new Event(eveId,eveName,eveDesc,userName,eveVenue,eveDate,eveCapacity);
        if(pst.add(e))
            System.out.println("Event added succesfully! ID:"+eveId);
        else
            System.out.println("Error, please try again!");
    }

    private static boolean replyToPost() {

        System.out.print("Enter post id or 'Q' to exit:");
        String postId = sc.nextLine();

        while (!postId.equalsIgnoreCase("q")){
            if(postId.substring(0,3).compareTo("EVE") == 0) {
                for (Event temp: pst) {
                    String id = temp.getId();
                    if(id.compareTo(postId) == 0) {
                        System.out.println(temp.getPostSummary());
                        System.out.print("\nEnter '1' to join event or 'Q' to quit:");
                        char getChoice = sc.nextLine().charAt(0);

                        if(getChoice == '1') {
                            Reply r = new Reply(id,Character.getNumericValue(getChoice),userName);

                            if(temp.checkReply(r) > 0)
                                System.out.println("Entry already exists!");
                            else {
                                if(temp.handleReply(r))
                                    System.out.println("Reply added successfully!");
                                else
                                    System.out.println("Problem!+++");
                            }
                        }
                    }
                    else {
                        System.out.println("Invalid post Id, Post not Found!");
                        break;
                    }
                }
            }
            else {
                System.out.println("Invalid post Id, Post not Found!");
                break;
            }
        }

        return false;
    }

    private static void displayAllPost() {
        for(Event temp: pst) {
            String details = temp.getPostDetails();
            System.out.println(details);
            System.out.println("------------------------------");
        }
    }

    private static void displayUserPost() {
        for(Event temp: pst) {
            if(userName.compareTo(temp.getcId()) == 0) {
                String details = temp.getPostDetails();
                System.out.println(details);
            }

            if(temp.getId().substring(0,3).compareTo("EVE") == 0) {
                System.out.print("\nAttendee List: ");
            }
            else {
                System.out.print("\nOffer History: \n============");
            }
            if(userName.compareTo(temp.getcId()) == 0) {
                System.out.println(temp.getReplyDetails()+" ");
                System.out.println("-------------------------------------");
            }
        }
    }

    private static void closePost() {
        System.out.print("Enter the post ID to close:");
        String id = sc.nextLine();
        for (Event temp: pst) {
            if(temp.getId().compareTo(id) == 0) {
                if(userName == temp.getcId()) {
                    temp.setStatus("CLOSED");
                    System.out.println("Post Closed!");
                }
                else {
                    System.out.println("Unable to close the post");
                }
            }
        }
    }

    private static void deletePost() {

        List<Event> toRemove = new ArrayList<Event>();

        System.out.print("Enter the post ID to delete:");
        String id = sc.nextLine();
        for (Event temp: pst) {
            if(temp.getId().compareTo(id) == 0) {
                if(userName.compareTo(temp.getcId()) == 0) {
                    toRemove.add(temp);
                }
                else {
                    System.out.println("Unable to delete the post");
                }
            }
        }
        pst.removeAll(toRemove);
        System.out.println("Post Removed!");
    }

    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }

    public static boolean validateDate(String strDate) {
        if (strDate.trim().equals("") || strDate == null) {
            return true;
        }
        else {
            SimpleDateFormat sdfrmt = new SimpleDateFormat("MM/dd/yyyy");
            sdfrmt.setLenient(false);
            try {
                Date javaDate = sdfrmt.parse(strDate);
            }
            catch (ParseException e) {
                return false;
            }
            return true;
        }
    }

    private static String generateId() {
        eventCount++;
        String id = "EVE"+ String.format("%03d",eventCount);

        return id;
    }

}

class Event {

    private String eveId, title, desc, status, cId, venue, date;
    private int capacity, attendeeCount;
    private static List<Reply> replies = new ArrayList<Reply>();

    public Event(String eveId, String title_c, String description_c, String creatorId_c, String venue_c, String date_c, int cap) {
        this.eveId = eveId;
        this.title = title_c;
        this.desc = description_c;
        this.cId = creatorId_c;
        venue = venue_c;
        date = date_c;
        capacity = cap;
        status = "OPEN";
        attendeeCount = 0;
    }

    public int checkReply(Reply re) {
        int match = 0;
        for(Reply temp : replies) {
            if(temp.getPostId().compareTo(re.getPostId()) == 0 && temp.getRespId().compareTo(re.getRespId()) == 0 && temp.getValue() == re.getValue()) {
                match++;
            }
        }
        return match;
    }

    public boolean handleReply(Reply reply) {
        boolean res = false;
        if(reply.getValue() == 1 && attendeeCount < capacity && status == "OPEN") {
            replies.add(reply);
            attendeeCount++;
            if( attendeeCount == capacity) {
               status = "CLOSED";
            }
            res = true;
        }
        return res;
    }

    public String getReplyDetails() {
        String attendees = "";
        for(Reply temp: replies) {
            if(temp.getPostId().compareTo(eveId) == 0)
                attendees = attendees + " " + temp.getRespId();
        }

        if (attendees.compareTo("") == 0)
            return "Empty";
        else
            return attendees;
    }

    public String getPostSummary() {
        return "\nTitle:\t\t"+ title + "\nDescription:\t" + desc+
                "\nVenue:\t\t" + venue + "\nDate:\t\t" + date + "\nStatus:\t\t" + status;
    }

    public String getPostDetails() {
        return "\nID:\t\t"+ getId() +"\nTitle:\t\t"+ title + "\nDescription:\t" + desc +
                "\nCreator Id:\t" + cId + "\nStatus:\t\t" + status + "\nVenue:\t\t" + venue +
                "\nDate:\t\t" + date + "\nCapacity:\t" + capacity + "\nAttendees:\t" + attendeeCount;
    }

    public void setStatus(String s){
        status = s;
    }

    public String getVenue() {
        return venue;
    }
    public String getDate() {
        return date;
    }
    public String getId() { return eveId; }
    public String getcId() { return cId; }
}

class Reply {

    private String postId;
    private int value;
    private String responderId;

    public Reply(String id, int val, String user) {
        postId = id;
        value = val;
        responderId = user;
    }

    public String getPostId() {
        return postId;
    }
    public int getValue() {
        return value;
    }
    public String getRespId() {
        return responderId;
    }
}