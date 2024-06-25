package com.eum.bank.timeBank.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;


@FeignClient(name = "haetsal-service", url = "${haetsal-url}")
public interface HaetsalClient {

    @GetMapping("/haetsal-service/api/v2/profile")
    HaetsalResponseDto.ProfileResponseBody getProfile(@RequestHeader String userId);

    @PostMapping("/haetsal-service/api/v2/timebank/users")
    List<HaetsalResponseDto.UserInfo> getUserInfos(@RequestBody HaetsalRequestDto.AccountNumberList accountNumbers);
}
