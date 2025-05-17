package com.capgemini.estimate.poc.estimate_api.usecase;

import com.capgemini.estimate.poc.estimate_api.domain.Estimate;
import java.io.ByteArrayOutputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class DownloadEstimateExcelUseCase {

  public byte[] execute() {
    List<Estimate> estimates =
        List.of(
            new Estimate(1L, "2025年度 サーバ見積", "株式会社A", 1000000000),
            new Estimate(2L, "2024年度 サーバ見積", "株式会社B", 2000000000));

    try (Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {

      Sheet sheet = workbook.createSheet("Estimates");

      Row header = sheet.createRow(0);
      header.createCell(0).setCellValue("見積ID");
      header.createCell(1).setCellValue("タイトル");
      header.createCell(2).setCellValue("顧客名");
      header.createCell(3).setCellValue("合計金額");

      for (int i = 0; i < estimates.size(); i++) {
        Estimate e = estimates.get(i);
        Row row = sheet.createRow(i + 1);
        row.createCell(0).setCellValue(e.id());
        row.createCell(1).setCellValue(e.title());
        row.createCell(2).setCellValue(e.customerName());
        row.createCell(3).setCellValue(e.totalAmount());
      }

      workbook.write(out);
      return out.toByteArray();

    } catch (Exception e) {
      throw new RuntimeException("Excel出力失敗", e);
    }
  }
}
