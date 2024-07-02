package com.hollroom.roommate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Alias("rmmt_board")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoommateDTO {
    private int roommate_id;
    private int id;
    private String title;
    private String content;
//    @JsonFormat(pattern = "yy-MM-dd", timezone = "Asia/Seoul")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul")
    private Date created_at;
    private String chat_room_id;
    private char nocturnal;
    private String wakeup_at;
    private int alarm;
    private String sleep_at;
    private char smoking;
    private char pet;
    private String cleaning_cycle;
    private String sleeping_habits;
    private char noise;
    private char deleted;
    private Date deleted_at;
    private Date updated_at;
    private String user_nickname;
}
