package com.thxpapa.juneberrydiary.domain.geo;

import com.thxpapa.juneberrydiary.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name="infra", schema="datamart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "infraUid", callSuper=false)
@ToString
public class Infra extends BaseTimeEntity {
    @Id
    @Column(name="infra_uid")
    private String infraUid;

    @Comment("인프라 이름")
    @Column(name="place_name", length = 45)
    private String placeName;

    @Comment("거리")
    @Column(name="distance", length = 45)
    private String distance;

    @Comment("인프라 등록 url")
    @Column(name="place_url", length = 45)
    private String placeUrl;

    @Comment("카테고리 이름")
    @Column(name="category_name", length = 45)
    private String categoryName;

    @Comment("지번")
    @Column(name="address_name", length = 45)
    private String addressName;

    @Comment("도로명 주소")
    @Column(name="road_address_name", length = 45)
    private String roadAddressName;

    @Comment("대표 핸드폰 번호")
    @Column(name="phone", length = 45)
    private String phone;

    @Comment("카테고리 그룹 코드")
    @Column(name="category_group_code", length = 45)
    private String categoryGroupCode;

    @Comment("카테고리 그룹 이름")
    @Column(name="category_group_name", length = 45)
    private String categoryGroupName;

    @Comment("경도")
    @Column(name="lon")
    private Double lon;

    @Comment("위도")
    @Column(name="lat")
    private Double lat;

    @Comment("상태정보")
    @Column(name="status_cd", length = 3, nullable = false)
    @ColumnDefault("'01'")
    private String statusCd;

    @Builder
    public Infra(String placeName, String distance, String placeUrl, String categoryName,
                 String addressName, String roadAddressName, String phone, String categoryGroupCode,
                 String categoryGroupName, Double lon, Double lat, String statusCd)
    {
        this.placeName = placeName;
        this.distance = distance;
        this.placeUrl = placeUrl;
        this.categoryName = categoryName;
        this.addressName = addressName;
        this.roadAddressName = roadAddressName;
        this.phone = phone;
        this.categoryGroupCode = categoryGroupCode;
        this.categoryGroupName = categoryGroupName;
        this.lon = lon;
        this.lat = lat;
        this.statusCd = statusCd;
    }
}
