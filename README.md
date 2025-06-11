# Budget Planning and Management System

A simple and modular **Java-based command-line application** that helps users track income, manage expenses, and monitor monthly category limits. Designed with object-oriented principles and robust testing support.

---

## Features

- Add income with source and timestamp
- Add expenses with categories and payment methods (with automatic fee calculation)
- Manage budget categories with monthly limits
- View financial summaries, net earnings, and filter transactions
- Track expenses over time ranges
- Detect and alert when a category exceeds its budget limit
- Auto-reset categories when a new month begins
- Full JUnit 5 test coverage + JaCoCo support

---

## Getting Started

### Prerequisites

- Java 17 or later
- Maven 3.6+ (if using `pom.xml`)
- JUnit 5 (included via Maven or IDE)

### Installation

Clone the repository:

```bash
git clone https://github.com/yourusername/budget-manager.git
cd budget-manager
```

Compile the project:

```bash
javac *.java
```

Or if using Maven:

```bash
mvn compile
```

---

## 🖥Running the Application

To launch the command-line interface:

```bash
java BudgetCLI
```

Then follow on-screen prompts such as:

```text
> add income
> add expense
> list transactions
> net earnings
```

Use `help` to see all supported commands.

---

## Testing

Run unit tests with:

```bash
mvn test
```

To generate test coverage report using JaCoCo:

```bash
mvn jacoco:report
```

Then open:

```
target/site/jacoco/index.html
```

You’ll find a complete line-by-line breakdown of which classes and methods were tested.

---

## Test Coverage Includes

- `TransactionTest` – Ensures abstract and shared logic works
- `IncomeTest`, `ExpenseTest` – Validate subclass behavior and fee handling
- `BudgetManagerTest` – Confirms transaction addition, filtering, earnings calculation
- `BudgetCategoryTest` – Tests limits, resets, and expense accumulation
- `MonthlyLimitExceededExceptionTest` – Verifies error information
- `EdgeCaseTest` – Includes extreme values, invalid inputs, and logic boundaries

---

## Technologies Used

- Java 17
- JUnit 5
- JaCoCo (Test Coverage)
- Maven (optional build support)
