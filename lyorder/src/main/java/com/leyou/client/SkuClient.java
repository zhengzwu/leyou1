package com.leyou.client;

import com.leyou.api.SkuApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface SkuClient extends SkuApi {
}
