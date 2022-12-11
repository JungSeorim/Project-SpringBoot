package com.app.milestone.service;

import com.app.milestone.domain.MoneyDTO;
import com.app.milestone.domain.Ranking;
import com.app.milestone.domain.SchoolDTO;
import com.app.milestone.entity.Donation;
import com.app.milestone.entity.Money;
import com.app.milestone.entity.People;
import com.app.milestone.entity.School;
import com.app.milestone.repository.DonationRepository;
import com.app.milestone.repository.MoneyRepository;
import com.app.milestone.repository.PeopleRepository;
import com.app.milestone.repository.SchoolRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoneyService {
    private final PeopleRepository peopleRepository;
    private final SchoolRepository schoolRepository;
    private final DonationRepository donationRepository;
    private final MoneyRepository moneyRepository;

    //    전체 기부금 랭킹
    public List<Ranking> donationMoneyRanking() {
        List<Ranking> arRanking = new ArrayList<>();
        List<Tuple> rankingInfo = moneyRepository.sortByMoneyCash();
        for (Tuple tuple : rankingInfo) {
            Ranking ranking = new Ranking();
            String peopleNickname = peopleRepository.findById(tuple.get(1, Long.TYPE)).get().getPeopleNickname();
            ranking.setPeopleNickname(peopleNickname);
            ranking.setUserId(tuple.get(1, Long.TYPE));
            ranking.setRankingItem(tuple.get(0, Long.TYPE));
            arRanking.add(ranking);
        }
        return arRanking;
    }

    //  보육원 하나에 대한 기부랭킹
    public List<MoneyDTO> moneyDonationRankingForOneSchool(Long userId) {
        List<MoneyDTO> moneyDTOS = new ArrayList<>();
        List<Tuple> tuples = moneyRepository.moneyRankingByOne(userId);
        for (Tuple tuple : tuples) {
            MoneyDTO moneyDTO = new MoneyDTO();
            String name = peopleRepository.findById(tuple.get(1, Long.class)).get().getPeopleNickname();
            moneyDTO.setMoneyCash(tuple.get(0, Long.class));
            moneyDTO.setPeopleNickname(name);
            moneyDTOS.add(moneyDTO);
        }

        return moneyDTOS;
    }

    //    최근 기부
    public List<MoneyDTO> recentDonationList(Long userId) {
        return moneyRepository.findByCreateDateByUserId(userId);
    }

    //    결제
    @Transactional
    public void payment(Long userId, MoneyDTO moneyDTO) {
        int donationCount = 0;
        People people = peopleRepository.findById(userId).get();

        School school = schoolRepository.findById(moneyDTO.getUserId()).get();

        Money money = new Money(school, people, moneyDTO.getMoneyCash());
        moneyRepository.save(money);
        donationCount = donationRepository.countByPeopleUserId(userId);
        people.update(donationCount);
        donationCount = donationRepository.countBySchoolUserId(moneyDTO.getUserId());
        school.update(donationCount);
    }


//    관리자 페이지=======================================================

    //    최신순, 금액 큰 순
    public Page<MoneyDTO> moneyListSearch(Integer page, String keyword) {
        if (page == null) page = 0;
        Pageable pageable = PageRequest.of(page, 7);
        if (keyword == null) {
            keyword = null;
        }
        List<MoneyDTO> list = moneyRepository.findMoneySearch(pageable, keyword);
        Page<MoneyDTO> money = new PageImpl<>(list, pageable, Integer.valueOf("" + moneyRepository.countByCreatedDate(pageable, keyword)));
        return money;
    }

    //    오래된 순, 금액 큰 순
    public Page<MoneyDTO> moneyListSearchAsc(Integer page, String keyword) {
        if (page == null) page = 0;
        Pageable pageable = PageRequest.of(page, 7);
        if (keyword == null) {
            keyword = null;
        }
        List<MoneyDTO> list = moneyRepository.findMoneySearchAsc(pageable, keyword);
        Page<MoneyDTO> money = new PageImpl<>(list, pageable, Integer.valueOf("" + moneyRepository.countByCreatedDate(pageable, keyword)));
        return money;
    }

    //    최신순, 금액 적은 순
    public Page<MoneyDTO> moneyListSearchAmount(Integer page, String keyword) {
        if (page == null) page = 0;
        Pageable pageable = PageRequest.of(page, 7);
        if (keyword == null) {
            keyword = null;
        }
        List<MoneyDTO> list = moneyRepository.findMoneySearchAmount(pageable, keyword);
        Page<MoneyDTO> money = new PageImpl<>(list, pageable, Integer.valueOf("" + moneyRepository.countByCreatedDate(pageable, keyword)));
        return money;
    }

    //    오래된 순, 금액 적은 순
    public Page<MoneyDTO> moneyListSearchAmountAsc(Integer page, String keyword) {
        if (page == null) page = 0;
        Pageable pageable = PageRequest.of(page, 7);
        if (keyword == null) {
            keyword = null;
        }
        List<MoneyDTO> list = moneyRepository.findMoneySearchAmountAsc(pageable, keyword);
        Page<MoneyDTO> money = new PageImpl<>(list, pageable, Integer.valueOf("" + moneyRepository.countByCreatedDate(pageable, keyword)));
        return money;
    }

}
