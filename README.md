# Last Value Price Service

## Version: 2.0

### Overview

This project implements a service for keeping track of the last price for financial instruments. Producers can publish prices, and consumers can request the latest price for a given instrument ID.

### Business Requirements

- **Price Data**: The price data consists of the following fields:
  - `id`: A string indicating which instrument the price refers to.
  - `asOf`: A date-time field indicating when the price was determined.
  - `payload`: The price data itself, which is a flexible data structure.

- **Batch Runs**: Producers can provide prices in batch runs:
  1. The producer indicates that a batch run is started.
  2. The producer uploads the records in the batch run in multiple chunks of 1000 records.
  3. The producer completes or cancels the batch run.

- **Consumer Requests**: Consumers can request the last price record for a given ID. The last value is determined by the `asOf` time set by the producer. On completion, all prices in a batch run should be made available at the same time. Batch runs which are cancelled can be discarded.

- **Resilience**: The service is resilient against producers which call the service methods in an incorrect order, or clients which call the service while a batch is being processed.

### Technical Requirements

- The service is implemented as a Java application.
- It is defined as a Java API, with producers and consumers running in the same JVM.
- It uses an in-memory solution rather than a database.
- Functionality is demonstrated through unit tests.
- Open source libraries are used, and dependencies are managed with Maven.

### Technologies Used

- **Java**
- **Spring Boot**
- **Spring Batch**
- **JUnit** for testing
- **Mockito** for mocking in tests
- **Maven** for dependency management


### Project Structure


![Screenshot 2024-06-06 205028](https://github.com/rishabhdev444/Assignment-SPGlobal/assets/53978955/c09e6721-1579-425c-8776-547a9054f751)


### Service Endpoints and Methods

#### 1. **startBatchRun(String batchId)**
   - Starts a new batch run identified by `batchId`.
   - Throws an `IllegalStateException` if a batch with the same ID already exists.
   
#### 2. **uploadRecords(String batchId, List<PriceRecord> records)**
   - Uploads a list of price records to an ongoing batch run identified by `batchId`.
   - Throws an `IllegalStateException` if the batch run is not started.

#### 3. **completeBatchRun(String batchId)**
   - Completes the batch run identified by `batchId` and processes the records.
   - Updates the latest prices for each instrument ID based on the `asOf` timestamp.
   - Throws an `IllegalStateException` if the batch run is not started.

#### 4. **cancelBatchRun(String batchId)**
   - Cancels the batch run identified by `batchId`.
   - Discards any records associated with the batch run.

#### 5. **getLastPrice(String id)**
   - Returns the latest price record for the given instrument ID.
   - Returns an empty `Optional` if no records are found for the instrument ID.
