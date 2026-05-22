# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Workspace Overview

This is a Windows home directory containing multiple Java projects in `eclipse-workspace/`. The primary project is **ConvFramework**, a multi-module document/file conversion pipeline framework. There are also standalone stock-analysis utilities and AWS S3 upload examples.

## Building (Apache Ant)

All ConvFramework modules use Apache Ant with a shared `common-targets.xml` at `eclipse-workspace/ConvFramework/common-targets.xml`.

Build a single module (run from within the module directory):
```
ant buildonly
```

This cleans, compiles, jars, and copies the `.jar` to `../lib/`. Modules build in dependency order: `CFWPublic` → `ConvDatabase` → `StorageAdapter` → `AESCipher` → `ConvCheckSum` → `AWDXMLServer` → `ConvFramework` → `DestLocAFT`.

**Required path:** Third-party JARs must exist at `D:/source/conversionTeam/libs/3rdPartyLibs`. This is hardcoded in `common-targets.xml`.

## Running the Framework

Entry point is `FrameProvider.main()`:
```
java -jar ConvFramework.jar <path-to-config.xml> <fully-qualified-config-class>
```

Example (from `sample.xml`):
```
java -jar ConvFramework.jar ./config/sample.xml com.dstsystems.bps.fw.imp.sample.config.SampleConfig
```

The license key in the config XML is a date string (`yyyy-MM-dd`); it must be a future date or the framework refuses to start.

## ConvFramework Architecture

The framework is a generic **producer-consumer pipeline** using a `LinkedBlockingQueue`. The flow:

1. `FrameProvider` loads and validates config (JAXB unmarshal), checks license, then spawns N `FrameConsumer` threads via a fixed thread pool.
2. `FrameProvider` calls the configured `IPopulateProcessItems.LoadQueue()` in a loop whenever the queue is empty, adding `IProcessItem` objects.
3. Each `FrameConsumer` blocks on `queue.take()` and calls `IProcessor.ProcessQueueObject()`.
4. Consumers can signal recovery via `RestartConsumerException` (retries up to 10 times with delays) or hard-stop via `ShutdownException`.

### Public Interfaces (`CFWPublic`)

All custom implementations go in dependent projects and reference only these interfaces:

| Interface | Purpose |
|---|---|
| `IConfig` | JAXB-annotated config bean; exposes license, threadCount, providerSleep, providerClass, consumerClass, endNoRecFound |
| `IPopulateProcessItems` | Producer: `KeepLoading()`, `LoadQueue()`, `StartupInitialize()`, `ShutdownCleanup()` |
| `IProcessItem` | Marker interface for work-unit objects passed through the queue |
| `IProcessor` | Consumer: `StartupInitialize()`, `ProcessQueueObject()`, `ShutdownCleanup()` |

### Implementing a New Pipeline

1. Implement `IConfig` with JAXB annotations matching your config XML element names.
2. Implement `IPopulateProcessItems` to load batches of work.
3. Implement `IProcessItem` to carry per-item data.
4. Implement `IProcessor` to process each item.
5. Reference class names in config XML under `<_providerClass>` and `<_consumerClass>`.

### Existing Concrete Modules

- **StorageAdapter** — adapters for Centera (EMC), HCP (Hitachi Content Platform), and AFT file systems
- **ConvDatabase** — JDBC utility (`ConvDB`, `Recordset`) wrapping SQL queries for conversion metadata
- **AESCipher** — AES encryption/decryption; `Bruter`/`HashBruter` for key-phrase analysis
- **AWDXMLServer** — socket server that handles AWD XML document collection requests
- **ConvCheckSum** — checksum computation for conversion verification
- **DestLocAFT** — concrete pipeline that reads destination-location records from DB and writes via AFT

## Stock Utilities

Standalone projects, no framework dependency:

- `stockPrice/` — scrapes Yahoo Finance HTML for a quote (parse not yet implemented)
- `StockPriceHistory/` — uses `yahoofinance` library to retrieve historical OHLCV data
- `SupportResistanceExample/` — calculates support/resistance levels from a price array
- `TslaSupportResistanceExample/` — same algorithm applied to TSLA

These are single-file programs; compile and run directly with `javac`/`java` or from Eclipse.
