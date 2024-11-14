package odiro.dto.location;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FestivalDto {
    private String addr1;
    private String addr2;
    private String eventStartDate;
    private String eventEndDate;
    private String firstImage;
    private String firstImage2;
    private String tel;
    private String title;

    // 생성자
    public FestivalDto(String addr1, String addr2, String eventStartDate, String eventEndDate,
                   String firstImage, String firstImage2, String tel, String title) {
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.firstImage = firstImage;
        this.firstImage2 = firstImage2;
        this.tel = tel;
        this.title = title;
    }

    // Getter와 Setter
    public String getAddr1() { return addr1; }
    public void setAddr1(String addr1) { this.addr1 = addr1; }

    public String getAddr2() { return addr2; }
    public void setAddr2(String addr2) { this.addr2 = addr2; }

    public String getEventStartDate() { return eventStartDate; }
    public void setEventStartDate(String eventStartDate) { this.eventStartDate = eventStartDate; }

    public String getEventEndDate() { return eventEndDate; }
    public void setEventEndDate(String eventEndDate) { this.eventEndDate = eventEndDate; }

    public String getFirstImage() { return firstImage; }
    public void setFirstImage(String firstImage) { this.firstImage = firstImage; }

    public String getFirstImage2() { return firstImage2; }
    public void setFirstImage2(String firstImage2) { this.firstImage2 = firstImage2; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}