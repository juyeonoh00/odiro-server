//package odiro.dto.location;
//
//import com.fasterxml.jackson.databind.PropertyNamingStrategies;
//import com.fasterxml.jackson.databind.annotation.JsonNaming;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//public class InformationVO {
//    private String contentid;
//    private String contenttypeid;
//    private String title;
//    private String createdtime;
//    private String modifiedtime;
//    private String tel;
//    private String telname;
//    private String homepage;
//    private String booktour;
//    private String firstimage;
//    private String firstimage2;
//    private String cpyrhtDivCd;
//    private String areacode;
//    private String sigungucode;
//    private String cat1;
//    private String cat2;
//    private String cat3;
//    private String addr1;
//    private String addr2;
//    private String zipcode;
//    private String mapx;
//    private String mapy;
//    private String mlevel;
//    private String overview;
//
//}
//
//<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
//  <response>
//    <header>
//      <resultCode>0000</resultCode>
//      <resultMsg>OK</resultMsg>
//    </header>
//    <body>
//      <items>
//
//        <item>
//          //<addr1>경상남도 김해시 분성로261번길 35 (봉황동)</addr1>
//          //<addr2>수릉원</addr2>
//          <booktour>
//          </booktour>
//          <cat1>A02</cat1>
//          <cat2>A0207</cat2>
//          <cat3>A02070200</cat3>
//          <contentid>694576</contentid>
//          <contenttypeid>15</contenttypeid>
//          <createdtime>20090218020108</createdtime>
//          //<eventstartdate>20241016</eventstartdate>
//          //<eventenddate>20241020</eventenddate>
//          //<firstimage>http://tong.visitkorea.or.kr/cms/resource/24/3354524_image2_1.JPG</firstimage>
//          //<firstimage2>http://tong.visitkorea.or.kr/cms/resource/24/3354524_image3_1.JPG</firstimage2>
//          <cpyrhtDivCd>Type3</cpyrhtDivCd>
//          <mapx>128.8759611362</mapx>
//          <mapy>35.2350661468</mapy>
//          <mlevel>6</mlevel>
//          <modifiedtime>20240827144230</modifiedtime>
//          <areacode>36</areacode>
//          <sigungucode>4</sigungucode>
//          //<tel>055-330-6840</tel>
//          //<title>가야문화축제</title>
//        </item>
//
//
//        <item>
//<addr1>서울특별시 강남구 영동대로 513 (삼성동)</addr1>
//          <addr2>
//          </addr2>
//          <booktour>
//          </booktour>
//          <cat1>A02</cat1>
//          <cat2>A0207</cat2>
//          <cat3>A02070200</cat3>
//          <contentid>737479</contentid>
//          <contenttypeid>15</contenttypeid>
//          <createdtime>20090521185913</createdtime>
//          <eventstartdate>20240926</eventstartdate>
//          <eventenddate>20240929</eventenddate>
//          <firstimage>http://tong.visitkorea.or.kr/cms/resource/39/2998139_image2_1.JPG</firstimage>
//          <firstimage2>http://tong.visitkorea.or.kr/cms/resource/39/2998139_image3_1.JPG</firstimage2>
//          <cpyrhtDivCd>Type3</cpyrhtDivCd>
//          <mapx>127.0592179950</mapx>
//          <mapy>37.5119175967</mapy>
//          <mlevel>6</mlevel>
//          <modifiedtime>20240829104312</modifiedtime>
//          <areacode>1</areacode>
//          <sigungucode>1</sigungucode>
//          <tel>02-3423-5543</tel>
//          <title>강남페스티벌</title>
//        </item>
//
//
//        <item>
//<addr1>서울특별시 강동구 동남로 870 (상일동)</addr1>
//          <addr2>강동아트센터</addr2>
//          <booktour>
//          </booktour>
//          <cat1>A02</cat1>
//          <cat2>A0207</cat2>
//          <cat3>A02070200</cat3>
//          <contentid>1806376</contentid>
//          <contenttypeid>15</contenttypeid>
//          <createdtime>20130422192127</createdtime>
//          <eventstartdate>20240928</eventstartdate>
//          <eventenddate>20240928</eventenddate>
//          <firstimage>http://tong.visitkorea.or.kr/cms/resource/62/3355062_image2_1.jpg</firstimage>
//          <firstimage2>http://tong.visitkorea.or.kr/cms/resource/62/3355062_image3_1.jpg</firstimage2>
//          <cpyrhtDivCd>Type3</cpyrhtDivCd>
//          <mapx>127.1574079432</mapx>
//          <mapy>37.5511370351</mapy>
//          <mlevel>6</mlevel>
//          <modifiedtime>20240828105426</modifiedtime>
//          <areacode>1</areacode>
//          <sigungucode>2</sigungucode>
//          <tel>02-471-0044</tel>
//          <title>강동북페스티벌</title>
//        </item>
//        <item>
//<addr1>서울특별시 강북구 도봉로76가길 55 (미아동)</addr1>
//          <addr2>
//          </addr2>
//          <booktour>
//          </booktour>
//          <cat1>A02</cat1>
//          <cat2>A0207</cat2>
//          <cat3>A02070200</cat3>
//          <contentid>3354973</contentid>
//          <contenttypeid>15</contenttypeid>
//          <createdtime>20240828100946</createdtime>
//          <eventstartdate>20240928</eventstartdate>
//          <eventenddate>20240928</eventenddate>
//          <firstimage>http://tong.visitkorea.or.kr/cms/resource/72/3354972_image2_1.png</firstimage>
//          <firstimage2>http://tong.visitkorea.or.kr/cms/resource/72/3354972_image3_1.png</firstimage2>
//          <cpyrhtDivCd>Type3</cpyrhtDivCd>
//          <mapx>127.0274174771</mapx>
//          <mapy>37.6321225012</mapy>
//          <mlevel>6</mlevel>
//          <modifiedtime>20240829113111</modifiedtime>
//          <areacode>1</areacode>
//          <sigungucode>3</sigungucode>
//          <tel>02-554-9795</tel>
//<title>강북청소년축제 강추</title>
//        </item>
//        <item>
//<addr1>강원특별자치도 춘천시 서면 박사로 882</addr1>
//          <addr2>
//          </addr2>
//          <booktour>
//          </booktour>
//          <cat1>A02</cat1>
//          <cat2>A0207</cat2>
//          <cat3>A02070200</cat3>
//          <contentid>2997769</contentid>
//          <contenttypeid>15</contenttypeid>
//          <createdtime>20230816111924</createdtime>
//          <eventstartdate>20241004</eventstartdate>
//          <eventenddate>20241006</eventenddate>
//          <firstimage>http://tong.visitkorea.or.kr/cms/resource/82/3346682_image2_1.jpg</firstimage>
//          <firstimage2>http://tong.visitkorea.or.kr/cms/resource/82/3346682_image3_1.jpg</firstimage2>
//          <cpyrhtDivCd>Type3</cpyrhtDivCd>
//          <mapx>127.6922325726</mapx>
//          <mapy>37.8955345910</mapy>
//          <mlevel>6</mlevel>
//          <modifiedtime>20240812172239</modifiedtime>
//          <areacode>32</areacode>
//          <sigungucode>13</sigungucode>
//          <tel>033-245-6320~1</tel>
//          <title>강콘페스타</title>
//        </item>
//        <item>
//<addr1>서울특별시 중구 을지로 지하12 (을지로1가)</addr1>
//          <addr2>
//          </addr2>
//          <booktour>
//          </booktour>
//          <cat1>A02</cat1>
//          <cat2>A0207</cat2>
//          <cat3>A02070200</cat3>
//          <contentid>2613900</contentid>
//          <contenttypeid>15</contenttypeid>
//          <createdtime>20190802003406</createdtime>
//          <eventstartdate>20240929</eventstartdate>
//          <eventenddate>20240929</eventenddate>
//          <firstimage>http://tong.visitkorea.or.kr/cms/resource/33/2998333_image2_1.png</firstimage>
//          <firstimage2>http://tong.visitkorea.or.kr/cms/resource/33/2998333_image3_1.png</firstimage2>
//          <cpyrhtDivCd>Type3</cpyrhtDivCd>
//          <mapx>126.9787960237</mapx>
//          <mapy>37.5655015943</mapy>
//          <mlevel>6</mlevel>
//          <modifiedtime>20240823131344</modifiedtime>
//          <areacode>1</areacode>
//          <sigungucode>24</sigungucode>
//          <tel>02-549-6111</tel>
//          <title>건강서울페스티벌</title>
//        </item>
//        <item>
//<addr1>서울특별시 용산구 두텁바위로 54-99 (용산동2가)</addr1>
//          <addr2>
//          </addr2>
//          <booktour>
//          </booktour>
//          <cat1>A02</cat1>
//          <cat2>A0208</cat2>
//          <cat3>A02081300</cat3>
//          <contentid>3348515</contentid>
//          <contenttypeid>15</contenttypeid>
//          <createdtime>20240814161154</createdtime>
//          <eventstartdate>20241001</eventstartdate>
//          <eventenddate>20241001</eventenddate>
//          <firstimage>http://tong.visitkorea.or.kr/cms/resource/14/3348514_image2_1.jpg</firstimage>
//          <firstimage2>http://tong.visitkorea.or.kr/cms/resource/14/3348514_image3_1.jpg</firstimage2>
//          <cpyrhtDivCd>Type3</cpyrhtDivCd>
//          <mapx>126.9798799337</mapx>
//          <mapy>37.5416099454</mapy>
//          <mlevel>6</mlevel>
//          <modifiedtime>20240819133107</modifiedtime>
//          <areacode>1</areacode>
//          <sigungucode>21</sigungucode>
//          <tel>02-3789-9560~4</tel>
//          <title>건군 76주년 국군의 날</title>
//        </item>
//        <item>
//<addr1>경기도 시흥시 은행동</addr1>
//          <addr2>601-150 은계호수공원 일원</addr2>
//          <booktour>
//          </booktour>
//          <cat1>A02</cat1>
//          <cat2>A0207</cat2>
//          <cat3>A02070200</cat3>
//          <contentid>3011169</contentid>
//          <contenttypeid>15</contenttypeid>
//          <createdtime>20230914174635</createdtime>
//          <eventstartdate>20241005</eventstartdate>
//          <eventenddate>20241006</eventenddate>
//          <firstimage>http://tong.visitkorea.or.kr/cms/resource/08/3334308_image2_1.jpeg</firstimage>
//          <firstimage2>http://tong.visitkorea.or.kr/cms/resource/08/3334308_image3_1.jpeg</firstimage2>
//          <cpyrhtDivCd>Type3</cpyrhtDivCd>
//          <mapx>126.8067956497</mapx>
//          <mapy>37.4446570072</mapy>
//          <mlevel>6</mlevel>
//          <modifiedtime>20240717211537</modifiedtime>
//          <areacode>31</areacode>
//          <sigungucode>14</sigungucode>
//          <tel>070-7769-5731</tel>
//<title>경기도 세계커피콩 축제</title>
//        </item>
//        <item>
//<addr1>경상남도 고성군 당항만로 1116</addr1>
//          <addr2>당항포관광지</addr2>
//          <booktour>
//          </booktour>
//          <cat1>A02</cat1>
//          <cat2>A0207</cat2>
//          <cat3>A02070200</cat3>
//          <contentid>141105</contentid>
//          <contenttypeid>15</contenttypeid>
//          <createdtime>20060414090000</createdtime>
//          <eventstartdate>20241002</eventstartdate>
//          <eventenddate>20241103</eventenddate>
//          <firstimage>http://tong.visitkorea.or.kr/cms/resource/62/2987562_image2_1.png</firstimage>
//          <firstimage2>http://tong.visitkorea.or.kr/cms/resource/62/2987562_image3_1.png</firstimage2>
//          <cpyrhtDivCd>Type3</cpyrhtDivCd>
//          <mapx>128.3915388600</mapx>
//          <mapy>35.0533471834</mapy>
//          <mlevel>6</mlevel>
//          <modifiedtime>20240812174022</modifiedtime>
//          <areacode>36</areacode>
//          <sigungucode>3</sigungucode>
//          <tel>055-670-7422</tel>
//          <title>경남고성공룡세계엑스포</title>
//        </item>
//        <item>
//<addr1>서울특별시 종로구 사직로 161 (세종로)</addr1>
//          <addr2>
//          </addr2>
//          <booktour>
//          </booktour>
//          <cat1>A02</cat1>
//          <cat2>A0207</cat2>
//          <cat3>A02070200</cat3>
//          <contentid>2648460</contentid>
//          <contenttypeid>15</contenttypeid>
//          <createdtime>20200224192834</createdtime>
//          <eventstartdate>20240911</eventstartdate>
//          <eventenddate>20241006</eventenddate>
//          <firstimage>http://tong.visitkorea.or.kr/cms/resource/24/3349624_image2_1.png</firstimage>
//          <firstimage2>http://tong.visitkorea.or.kr/cms/resource/24/3349624_image3_1.png</firstimage2>
//          <cpyrhtDivCd>Type3</cpyrhtDivCd>
//          <mapx>126.9767375783</mapx>
//          <mapy>37.5760836609</mapy>
//          <mlevel>6</mlevel>
//          <modifiedtime>20240820131220</modifiedtime>
//          <areacode>1</areacode>
//          <sigungucode>23</sigungucode>
//          <tel>1522-2295</tel>
//<title>경복궁 별빛야행</title>
//        </item>
//      </items>
//      <numOfRows>10</numOfRows>
//      <pageNo>1</pageNo>
//      <totalCount>328</totalCount>
//    </body>
//  </response>