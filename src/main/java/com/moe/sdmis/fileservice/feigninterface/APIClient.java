//package com.moe.sdmis.fileservice.feigninterface;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//@FeignClient(value = "MOE-PREFILL-EXCEL-SERVICE", url = "http://localhost:8098")
//public interface APIClient {
//	@GetMapping(value = "/sdmis/prefill/{schoolId}")
//    void downloadPrefillUdiseTemplate(@PathVariable("schoolId") String schoolId);
//}
