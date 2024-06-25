package com.eum.bank.timeBank.client;

import lombok.Getter;
import lombok.Setter;

public class HaetsalResponseDto {

    @Getter
    @Setter
    public static class UserInfo {
        private int userId;
        private String nickName;
        private String profileImage;
    }

    @Getter
    @Setter
    public static class ProfileResponseBody{
        private int status;
        private String code;
        private String msg;
        private String detailMsg;
        private String reason; // error 일 경우 출력

        public Profile data;
    }

    @Getter
    @Setter
    public static class Profile {
        private int userId;
        private String nickName;
        private String accountNumber;
        private String profileImage;
    }

}
