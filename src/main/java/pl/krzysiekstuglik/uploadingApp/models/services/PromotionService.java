package pl.krzysiekstuglik.uploadingApp.models.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Service
public class PromotionService {

    @Value("${monday.promotion.max.file.size}")
    @Getter
    private int mondayPromotionMaxFileSize;

    public boolean isMondeyToday(){
        return LocalDateTime.now().getDayOfWeek() == DayOfWeek.MONDAY;

    }
}
