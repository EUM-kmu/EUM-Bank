package com.eum.bank.timeBank.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HaetsalProfileResponse {
    private int status;
    private String code;
    private String msg;
    private String detailMsg;
    private Data data;

    @Getter
    @Setter
    public class Data {
        private int userId;
        private String nickName;
//        private String gender;
//        private String address;
//        private int ageRange;
//        private String birth;
        private String accountNumber;
        private String profileImage;
//        private boolean blocked;
    }
}
