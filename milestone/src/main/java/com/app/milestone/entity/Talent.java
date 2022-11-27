package com.app.milestone.entity;

import com.app.milestone.type.Category;
import com.app.milestone.type.Place;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_TALENT")
@DiscriminatorValue("talent")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Talent extends Donation {
    private String talentTitle;
    private String talentContent;
    private LocalDateTime talentAbleDate;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Enumerated(EnumType.STRING)
    private Place place;

    @Builder
    public Talent(String talentTitle, String talentContent, LocalDateTime talentAbleDate, Category category, Place place) {
        this.talentTitle = talentTitle;
        this.talentContent = talentContent;
        this.talentAbleDate = talentAbleDate;
        this.category = category;
        this.place = place;
    }

    public void update(String talentTitle, String talentContent, LocalDateTime talentAbleDate, Category category, Place place) {
        this.talentTitle = talentTitle;
        this.talentContent = talentContent;
        this.talentAbleDate = talentAbleDate;
        this.category = category;
        this.place = place;
    }
}