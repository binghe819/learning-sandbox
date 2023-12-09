package com.binghe.springcloudopenfeign.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "publicApiClient", url = "${external.public-api.host}")
public interface PublicApiClient {

    @GetMapping(value = "/entries")
    GetEntryResponse getEntries(@RequestParam(value = "title", required = false) String title,
                                @RequestParam(value = "description", required = false) String description,
                                @RequestParam(value = "auth", required = false) String auth,
                                @RequestParam(value = "https", required = false) boolean https,
                                @RequestParam(value = "cors", required = false) String cors,
                                @RequestParam(value = "category", required = false) String category);
}
