package com.sap.exciseduty.utility;

import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.customtheexcisedutyproject.ExciseDutyComplex;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.customtheexcisedutyprojectperf.ED_PERF_CDS;
import com.sap.exciseduty.client.s4worklist.pojos.S4WorklistItem;

public class VdmMapper {

    /*
     * Collection mapping helper
     */
    public static <VDM_ENTITY, OBJECT> List<OBJECT> mapCollection(Collection<VDM_ENTITY> entities, Function<VDM_ENTITY, OBJECT> mapAlert) {
        return entities.stream().map(mapAlert).collect(Collectors.toList());
    }

    public static S4WorklistItem mapWorklistItem(ExciseDutyComplex vdmEntity) {
        S4WorklistItem item = new S4WorklistItem();
        item.setMaterialDocumentNumber(vdmEntity.getMblnr());
        item.setMaterialYear(vdmEntity.getMjahr());
        item.setLine(vdmEntity.getZeile());
        item.setBillOfMaterialItemNodeNumber(vdmEntity.getStlkn());
        item.setMaterialNumber(vdmEntity.getMatnr());
        item.setProcessingStatus(vdmEntity.getStats());
        item.setMovementType(vdmEntity.getBwart());
        item.setAutomaticallyCreatedCharacteristic(vdmEntity.getXauto());
        item.setPlant(vdmEntity.getWerks());
        item.setCompanyCode(vdmEntity.getBukrs());
        item.setStorageLocation(vdmEntity.getLgort());
        item.setBatchNumber(vdmEntity.getCharg());
        item.setSpecialStock(vdmEntity.getSobkz());
        item.setOrderNumber(vdmEntity.getAufnr());
        item.setMovementIndicator(vdmEntity.getKzbew());
        item.setVendorAccountNumber(vdmEntity.getLifnr());
        item.setCustomerNumber(vdmEntity.getKunnr());
        item.setSalesOrderNumber(vdmEntity.getKdauf());
        item.setSalesOrderItemNumber(vdmEntity.getKdpos());
        item.setSalesDocument(vdmEntity.getVbeln());
        item.setSalesDocumentType(vdmEntity.getAuart());
        item.setOrderReason(vdmEntity.getAugru());
        item.setSalesDocumentItemCategory(vdmEntity.getPstyv());
        item.setSoldToParty(vdmEntity.getKunag());
        item.setCustomerNumberForIntercompanyBilling(vdmEntity.getKuniv());
        item.setDebitorCreditorIndicator(vdmEntity.getShkzg());
        item.setQuantity(vdmEntity.getMenge());
        item.setBaseUnitOfMeasure(vdmEntity.getMeins());
        item.setPurchsaeOrderNumber(vdmEntity.getEbeln());
        item.setPurchaseOrderItemNumber(vdmEntity.getEbelp());
        item.setPurchaseOrderHistory(vdmEntity.getVgabe());
        item.setNumberOfMaterialDocument(vdmEntity.getBelnr());
        item.setPurchaseDocumentItemCategory(vdmEntity.getPstyp());
        item.setItemText(vdmEntity.getSgtxt());
        item.setGoodsRecipient(vdmEntity.getWempf());
        item.setCostCenter(vdmEntity.getKostl());
        // item.setPostingDateInDocument(vdmEntity.getBudat() != null ?
        // vdmEntity.getBudat().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null);
        item.setPostingDateInDocument(vdmEntity.getBudat() != null ? vdmEntity.getBudat().toLocalDate() : null);
        return item;
    }

    public static ExciseDutyComplex mapExciseDutyComplex(ED_PERF_CDS vdmEntity) {
        ExciseDutyComplex item = new ExciseDutyComplex();
        item.setMblnr(vdmEntity.getMblnr());
        item.setMjahr(vdmEntity.getMjahr());
        item.setZeile(vdmEntity.getZeile());
        item.setStlkn(vdmEntity.getStlkn());
        item.setMatnr(vdmEntity.getMatnr());
        item.setStats(vdmEntity.getStats());
        item.setBwart(vdmEntity.getBwart());
        item.setXauto(vdmEntity.getXauto());
        item.setWerks(vdmEntity.getWerks());
        item.setBukrs(vdmEntity.getBukrs());
        item.setLgort(vdmEntity.getLgort());
        item.setCharg(vdmEntity.getCharg());
        item.setSobkz(vdmEntity.getSobkz());
        item.setAufnr(vdmEntity.getAufnr());
        item.setKzbew(vdmEntity.getKzbew());
        item.setLifnr(vdmEntity.getLifnr());
        item.setKunnr(vdmEntity.getKunnr());
        item.setKdauf(vdmEntity.getKdauf());
        item.setKdpos(vdmEntity.getKdpos());
        item.setVbeln(vdmEntity.getVbeln());
        item.setAuart(vdmEntity.getAuart());
        item.setAugru(vdmEntity.getAugru());
        item.setPstyv(vdmEntity.getPstyv());
        item.setKunag(vdmEntity.getKunag());
        item.setKuniv(vdmEntity.getKuniv());
        item.setShkzg(vdmEntity.getShkzg());
        item.setMenge(vdmEntity.getMenge());
        item.setMeins(vdmEntity.getMeins());
        item.setEbeln(vdmEntity.getEbeln());
        item.setEbelp(vdmEntity.getEbelp());
        item.setVgabe(vdmEntity.getVgabe());
        item.setBelnr(vdmEntity.getBelnr());
        item.setPstyp(vdmEntity.getPstyp());
        item.setSgtxt(vdmEntity.getSgtxt());
        item.setWempf(vdmEntity.getWempf());
        item.setKostl(vdmEntity.getKostl());
        item.setBudat(vdmEntity.getBudat());
        return item;
    }
}
