This repository contains code for UI Layer Automation of Android App and Android and iOS Native Web App. Tools Used are Appium and TestNG.
Here's the project folder structure:
MobileApp_UIAutomation/
├── .github/
│   └── workflows/           # CI/CD workflows for GitHub Actions
├── reports/                 # Test reports generated after executions 📱
├── scripts/                 # Utility and setup scripts 📱
├── src/                     # Source code for the application and tests
│   ├── main/                # Main application code
│   │   ├── java/
│   │   │   ├── basePage/    # Base page classes for Appium 📱
│   │   │   ├── envconfig/   # Environment configurations for Appium 📱
│   │   │   ├── pages/       # Appium page objects 📱
│   │   │   └── utils/       # Utility classes for Appium 📱
│   └── test/                # Test code for Appium
│       ├── java/            # Java test classes
│       │   ├── baseTest/    # Base test classes 📱
│       │   ├── reporting/   # Reporting utilities 📱
│       │   └── tests/       # Appium test cases 📱
├── videos/                  # Video recordings of test executions 🎥
├── pom.xml                  # Maven build configuration file
└── README.md                # Project documentation
