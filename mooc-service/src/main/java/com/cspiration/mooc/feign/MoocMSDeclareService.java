package com.cspiration.mooc.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient("cson-mooc-ms")
@RestController
public interface MoocMSDeclareService {

    @GetMapping("/demoGet")
    public Long demoGet(@RequestParam Long id);

    @PostMapping("demoPost")
    public Map<String, Object> demoPost(@RequestBody Map<String, Object> params);

    @GetMapping("/timeout")
    public String timeout(@RequestParam Long time);
}
