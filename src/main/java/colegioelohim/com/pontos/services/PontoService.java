package colegioelohim.com.pontos.services;

import colegioelohim.com.local.entities.LocalEntity;
import colegioelohim.com.local.service.LocalService;
import colegioelohim.com.pontos.dtos.BaterPontoRequestDTO;
import colegioelohim.com.pontos.dtos.PontoResponseDTO;
import colegioelohim.com.pontos.dtos.PontoUsuarioDTO;
import colegioelohim.com.pontos.entities.PontosEntity;
import colegioelohim.com.pontos.repository.PontosRepository;
import colegioelohim.com.users.entities.UserEntity;
import colegioelohim.com.utils.GeoUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PontoService {

    @Inject
    LocalService localService;

    @Inject
    JsonWebToken jwt;

    @Inject
    PontosRepository repository;



    public List<PontoResponseDTO> listarPontos(UUID usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream()
                .map(p -> new  PontoResponseDTO(p.dataHora,
                        p.valido,
                        p.motivoInvalidacao))
                .toList();
    }

    public List<PontoResponseDTO> listarPontosDoUsuario(UUID usuarioId) {
        return repository.findByUsuarioId(usuarioId)
                .stream()
                .map(p -> new PontoResponseDTO(
                        p.dataHora,
                        p.valido,
                        p.motivoInvalidacao
                ))
                .toList();
    }

    private void setBorders(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    public byte[] gerarExcelUsuario(UUID usuarioId) {

        List<PontoUsuarioDTO> pontos = repository.listarPontosComUsuario(usuarioId);



        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Pontos");


            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            setBorders(headerStyle);

            CellStyle normalStyle = workbook.createCellStyle();
            normalStyle.setAlignment(HorizontalAlignment.LEFT);
            setBorders(normalStyle);

            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.cloneStyleFrom(normalStyle);
            dateStyle.setDataFormat(
                    workbook.getCreationHelper()
                            .createDataFormat()
                            .getFormat("dd/MM/yyyy HH:mm")
            );

            CellStyle invalidStyle = workbook.createCellStyle();
            invalidStyle.cloneStyleFrom(normalStyle);
            invalidStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            invalidStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);


            Row header = sheet.createRow(0);
            header.setHeightInPoints(22);

            String[] columns = {
                    "Nome",
                    "Email",
                    "Data/Hora",
                    "Válido",
                    "Motivo da Invalidação"
            };

            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }


            int rowIndex = 1;
            for (PontoUsuarioDTO ponto : pontos) {

                boolean invalido = !ponto.valido;
                CellStyle rowStyle = invalido ? invalidStyle : normalStyle;

                Row row = sheet.createRow(rowIndex++);

                Cell c0 = row.createCell(0);
                c0.setCellValue(ponto.nome);
                c0.setCellStyle(rowStyle);

                Cell c1 = row.createCell(1);
                c1.setCellValue(ponto.email);
                c1.setCellStyle(rowStyle);

                Cell c2 = row.createCell(2);
                c2.setCellValue(
                        java.sql.Timestamp.valueOf(ponto.dataHora)
                );
                c2.setCellStyle(invalido ? invalidStyle : dateStyle);

                Cell c3 = row.createCell(3);
                c3.setCellValue(ponto.valido ? "Sim" : "Não");
                c3.setCellStyle(rowStyle);

                Cell c4 = row.createCell(4);
                c4.setCellValue(
                        ponto.motivoInvalidacao != null
                                ? ponto.motivoInvalidacao
                                : ""
                );
                c4.setCellStyle(rowStyle);
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            sheet.createFreezePane(0, 1);

            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar Excel do usuário", e);
        }
    }


    @Transactional
    public PontosEntity baterPonto(BaterPontoRequestDTO dto) {

        UserEntity usuario = UserEntity.findById(
                UUID.fromString(jwt.getSubject())
        );

        LocalEntity local = localService.getLocalPadrao();

        double distancia = GeoUtils.calcularDistanciaMetros(
                local.latitude,
                local.longitude,
                dto.latitude,
                dto.longitude
        );

        boolean dentroDaArea = distancia <= local.raioPermitidoMetros;
        LocalDateTime horaSubtraida = LocalDateTime.now().minusHours(3);


        PontosEntity ponto = new PontosEntity();
        ponto.usuario = usuario;
        ponto.local = local;
        ponto.dataHora = horaSubtraida;
        ponto.latitude = dto.latitude;
        ponto.longitude = dto.longitude;
        ponto.valido = dentroDaArea;

        if (!dentroDaArea) {
            ponto.motivoInvalidacao = "Fora da área permitida";
        }

        ponto.persist();
        return ponto;

    }



}
