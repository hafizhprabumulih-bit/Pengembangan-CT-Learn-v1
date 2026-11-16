package com.example.data

/**
 * Representasi spesifikasi teknis perangkat keras pesawat CT Scan untuk kebutuhan
 * edukasi instrumentasi radiologi (Tabung Sinar-X, HV Generator, Slip-Ring,
 * Bow-Tie Filter & Kolimator, Busur Detektor MSCT/PCD, DAS, dan Meja Pasien).
 */
data class CTScannerInstrument(
    val id: String,
    val modelName: String,
    val generationLabel: String,
    val gantryApertureCm: Float,
    val gantryTiltRangeDeg: Int,
    val minRotationTimeSec: Float,
    val tubeHeatCapacity: String,
    val tubeHeatCapacityMhu: Float,
    val anodeCoolingRateKhuPerMin: Int,
    val anodeBearingType: String,
    val focalSpotSizesMm: String,
    val hasFlyingFocalSpot: Boolean,
    val kvpRange: List<Int>,
    val maxTubeCurrentMa: Int,
    val hvGeneratorType: String,
    val slipRingType: String,
    val opticalBandwidthGbps: Float,
    val bowTieFilterOptions: List<String>,
    val collimatorSystem: String,
    val detectorType: String,
    val detectorArrayConfiguration: String,
    val detectorRows: Int,
    val minSliceThicknessMm: Float,
    val maxBeamCollimationMm: Float = detectorRows * minSliceThicknessMm,
    val detectorQdePercent: Float,
    val dasSamplingRateViewsPerRot: Int,
    val dasAdcBitDepth: Int,
    val tableMaterial: String = "Carbon Fiber Reinforced Polymer (Z = 6, akurasi ±0,25 mm)",
    val laserPositioningAxes: String = "Laser 3-Sumbu (Aksial, Sagital, Koronal Isocenter)"
) {
    /** Total lebar kolimasi berkas sinar-X sepanjang sumbu-z: W_total = N × T (mm). */
    fun calculateBeamCollimationMm(): Float = maxBeamCollimationMm

    /** Estimasi beban termal anoda (Heat Units) per eksposi: HU = 1,4 × kVp × mA × t_scan. */
    fun calculateHeatUnits(kvp: Int, ma: Int, scanTimeSec: Float): Float =
        1.4f * kvp * ma * scanTimeSec

    /** Perhitungan IEC Helical Beam Pitch: Pitch = Table Feed per Rotasi (d) / (N × T). */
    fun calculatePitch(tableFeedMmPerRot: Float): Float {
        val totalCollimation = calculateBeamCollimationMm()
        return if (totalCollimation > 0f) tableFeedMmPerRot / totalCollimation else 0f
    }

    fun toSummaryText(): String =
        "$modelName ($generationLabel) | Tabung: $tubeHeatCapacity | Slip-Ring: $slipRingType | Detektor: $detectorArrayConfiguration"
}

object CTScannerInstrumentCatalog {
    val presets: List<CTScannerInstrument> = listOf(
        CTScannerInstrument(
            id = "ct_msct_64_workhorse",
            modelName = "MSCT 64-Slice Diagnostik Klinis",
            generationLabel = "Generasi ke-3 (Rotate-Rotate Wide Fan Beam)",
            gantryApertureCm = 70f,
            gantryTiltRangeDeg = 30,
            minRotationTimeSec = 0.35f,
            tubeHeatCapacity = "6.5 MHU (Anoda Paduan Tungsten-Rhenium-Graphite, Pendingin Oli-Udara)",
            tubeHeatCapacityMhu = 6.5f,
            anodeCoolingRateKhuPerMin = 1100,
            anodeBearingType = "Liquid Metal Bearing (Gallium-Indium-Tin), 9.000 RPM",
            focalSpotSizesMm = "0,6 × 0,7 mm (Small) / 1,1 × 1,2 mm (Large)",
            hasFlyingFocalSpot = false,
            kvpRange = listOf(80, 100, 120, 140),
            maxTubeCurrentMa = 665,
            hvGeneratorType = "High-Frequency Inverter (40 kHz) di atas Rotating Frame, ripple < 1%",
            slipRingType = "Low-Voltage Slip-Ring (480 V, Sikat Perak-Grafit) + Link Data Optik Laser",
            opticalBandwidthGbps = 10f,
            bowTieFilterOptions = listOf("Small/Head", "Medium", "Large/Body"),
            collimatorSystem = "Pre-Patient Tungsten Blades (z-axis) + Post-Patient Anti-Scatter Septa",
            detectorType = "Solid-State Scintillator (Gd2O2S / GOS) + Silicon Photodiode",
            detectorArrayConfiguration = "64 × 0,625 mm (Matrix Array, total kolimasi 40 mm)",
            detectorRows = 64,
            minSliceThicknessMm = 0.625f,
            maxBeamCollimationMm = 40.0f,
            detectorQdePercent = 98.0f,
            dasSamplingRateViewsPerRot = 1200,
            dasAdcBitDepth = 20
        ),
        CTScannerInstrument(
            id = "ct_dsct_cardiac",
            modelName = "Dual-Source CT (DSCT) Kardiak & Dual-Energy",
            generationLabel = "Generasi ke-3 Dual-Source (2 Tabung & 2 Detektor bersudut 95°)",
            gantryApertureCm = 78f,
            gantryTiltRangeDeg = 0,
            minRotationTimeSec = 0.25f,
            tubeHeatCapacity = "Rotating-Envelope Direct-Cooling Tube (disipasi efektif 5.000+ kHU/mnt)",
            tubeHeatCapacityMhu = 8.0f,
            anodeCoolingRateKhuPerMin = 5000,
            anodeBearingType = "Hydrodynamic Liquid Metal Bearing, 10.000 RPM",
            focalSpotSizesMm = "0,4 × 0,5 mm / 0,8 × 1,1 mm + z-Flying Focal Spot (z-FFS)",
            hasFlyingFocalSpot = true,
            kvpRange = listOf(70, 80, 90, 100, 120, 140, 150),
            maxTubeCurrentMa = 1300,
            hvGeneratorType = "Dual High-Frequency Inverter HV Generator (100 kHz) di atas Rotating Frame",
            slipRingType = "Low-Voltage Slip-Ring Daya Ganda + Transceiver Optik Multi-Saluran 25 Gbps",
            opticalBandwidthGbps = 25f,
            bowTieFilterOptions = listOf("Head", "Body", "Tin-Filter (Sn100/Sn150) Spectral"),
            collimatorSystem = "Adaptive Dose Shield Pre-Patient Collimator + 2D Focused Anti-Scatter Grid",
            detectorType = "Ultra-Fast Ceramics (UFC) Scintillator + Integrated CMOS Photodiode",
            detectorArrayConfiguration = "2 × (96 × 0,6 mm) dengan z-FFS (192 slice per sumber)",
            detectorRows = 96,
            minSliceThicknessMm = 0.6f,
            maxBeamCollimationMm = 57.6f,
            detectorQdePercent = 99.0f,
            dasSamplingRateViewsPerRot = 2400,
            dasAdcBitDepth = 24
        ),
        CTScannerInstrument(
            id = "ct_wide_320_volume",
            modelName = "Wide-Area Cone-Beam 320-Row Volume CT",
            generationLabel = "Generasi ke-3 Wide-Area Volumetric Cone-Beam",
            gantryApertureCm = 78f,
            gantryTiltRangeDeg = 30,
            minRotationTimeSec = 0.275f,
            tubeHeatCapacity = "7.5 MHU High-Endurance Anode + Heat Exchanger Oli-Udara Berkapasitas Tinggi",
            tubeHeatCapacityMhu = 7.5f,
            anodeCoolingRateKhuPerMin = 1500,
            anodeBearingType = "Liquid Metal Bearing (Ga-In-Sn), 9.500 RPM",
            focalSpotSizesMm = "0,6 × 0,6 mm (Small) / 1,0 × 1,4 mm (Large)",
            hasFlyingFocalSpot = false,
            kvpRange = listOf(80, 100, 120, 135),
            maxTubeCurrentMa = 900,
            hvGeneratorType = "High-Frequency Inverter Generator (100 kW) pada Rotating Frame",
            slipRingType = "Low-Voltage Slip-Ring + Transmisi Data Optik Kecepatan Tinggi 40 Gbps",
            opticalBandwidthGbps = 40f,
            bowTieFilterOptions = listOf("Small/Neuro", "Medium/Cardiac", "Large/Body"),
            collimatorSystem = "Dynamic Cone-Beam Pre-Patient Collimator + 2D Tungsten Anti-Scatter Grid",
            detectorType = "PureVision Solid-State Scintillator + Photodiode Array",
            detectorArrayConfiguration = "320 × 0,5 mm (Matrix Array Cakupan 160 mm dalam 1 Rotasi)",
            detectorRows = 320,
            minSliceThicknessMm = 0.5f,
            maxBeamCollimationMm = 160.0f,
            detectorQdePercent = 98.5f,
            dasSamplingRateViewsPerRot = 1800,
            dasAdcBitDepth = 22
        ),
        CTScannerInstrument(
            id = "ct_pcd_spectral",
            modelName = "Photon-Counting Detector CT (PCD-CT)",
            generationLabel = "Generasi Mutakhir Direct-Conversion Spectral CT",
            gantryApertureCm = 82f,
            gantryTiltRangeDeg = 0,
            minRotationTimeSec = 0.25f,
            tubeHeatCapacity = "Vectron Direct-Cooled X-Ray Tube (disipasi termal kontinu ultra-tinggi)",
            tubeHeatCapacityMhu = 8.5f,
            anodeCoolingRateKhuPerMin = 5400,
            anodeBearingType = "Liquid Metal Bearing, 10.000 RPM",
            focalSpotSizesMm = "0,4 × 0,5 mm Ultra-Fine Focal Spot",
            hasFlyingFocalSpot = true,
            kvpRange = listOf(70, 90, 120, 140),
            maxTubeCurrentMa = 1000,
            hvGeneratorType = "Precision High-Frequency Inverter HV Generator pada Rotating Frame",
            slipRingType = "Low-Voltage Slip-Ring + High-Throughput Optical Data Ring 40+ Gbps",
            opticalBandwidthGbps = 40f,
            bowTieFilterOptions = listOf("Standard Spectral", "Tin-Filtered Low-Dose"),
            collimatorSystem = "2D Focused Pre- & Post-Patient Collimator Bebas Septa Sintilator Tebal",
            detectorType = "Direct-Conversion Semiconductor Cadmium Telluride (CdTe) Photon-Counting",
            detectorArrayConfiguration = "144 × 0,4 mm Standard / Ultra-High-Res 0,2 mm Sub-Piksel + 4 Energy Bins",
            detectorRows = 144,
            minSliceThicknessMm = 0.2f,
            maxBeamCollimationMm = 57.6f,
            detectorQdePercent = 99.5f,
            dasSamplingRateViewsPerRot = 2400,
            dasAdcBitDepth = 24
        )
    )
}
