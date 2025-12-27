# Authentication Service

A secure authentication and authorization service built with Spring Boot that provides JWT (JSON Web Token) based authentication and token management.

## Overview

The Authentication Service is a microservice that handles:
- User authentication and JWT token generation
- Access token verification and validation
- Refresh token management
- Multi-language support (English, Hindi)
- Comprehensive error handling with localized error messages

## Project Information

- **GroupId**: `com.common`
- **ArtifactId**: `authentication-service`
- **Version**: `0.0.1`
- **Java Version**: `17`
- **Spring Boot Version**: `4.0.1`
- **Build Tool**: Maven

## Technology Stack

### Core Dependencies
- **Spring Boot 4.0.1**
- **Spring Security**: For cryptographic operations
- **JWT Libraries**:
  - jjwt-api v0.11.5
  - jjwt-impl v0.11.5
  - jjwt-jackson v0.11.5
  - java-jwt v3.18.2

### Security & Utilities
- **BCrypt**: Password hashing (jbcrypt v0.4)
- **Lombok**: Reduce boilerplate code
- **Apache Commons Lang3**: Utility functions

### Internal Dependencies
- **exception-handler** 0.0.1: Common exception handling
- **common-utils** 0.0.1: Shared utilities

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/common/oauth/
│   │       ├── AuthenticationServiceApplication.java
│   │       ├── controller/
│   │       │   └── JwtTokenController.java
│   │       ├── exception/
│   │       │   └── ErrorCode.java
│   │       ├── model/
│   │       │   ├── JwtToken.java
│   │       │   ├── Token.java
│   │       │   ├── RefreshTokensDto.java
│   │       │   └── UserAgentDetails.java
│   │       ├── service/
│   │       │   ├── AuthenticationService.java
│   │       │   ├── JwtAuthenticationService.java
│   │       │   └── AESCryptography.java
│   │       └── util/
│   │           ├── Base64.java
│   │           ├── BycryptHasingUtil.java
│   │           ├── CommonUtil.java
│   │           ├── Constants.java
│   │           ├── JwtTokenHelper.java
│   │           ├── TokenHelper.java
│   │           ├── UserAgentParser.java
│   │           └── UserAgentParseException.java
│   └── resources/
│       ├── application.properties
│       ├── messages.properties
│       ├── messages_en_IN.properties
│       └── messages_hi_IN.properties
└── test/
```

## API Endpoints

### Base URL
`/api/v1/tokens`

### Endpoints

#### 1. Generate JWT Token
- **Method**: `POST`
- **Endpoint**: `/api/v1/tokens`
- **Headers**: 
  - `User-Agent` (required)
- **Request Body**: JwtToken object
- **Response**: Generated JWT Token with access and refresh tokens

#### 2. Verify Access Token
- **Method**: `POST`
- **Endpoint**: `/api/v1/tokens/verify/access`
- **Headers**: 
  - `User-Agent` (required)
- **Request Body**: JwtToken object with accessToken
- **Response**: Verified token details

#### 3. Refresh Access Token
- **Method**: `POST`
- **Endpoint**: `/api/v1/tokens/refresh/access`
- **Headers**: 
  - `User-Agent` (required)
- **Request Body**: JwtToken object with refreshToken
- **Response**: New access token

## Error Codes and Messages

The service supports localized error messages in English and Hindi. All error codes are defined in the `ErrorCode` enum and their corresponding messages are in the properties files.

### Error Codes (13 Total)

| Error Code | English Message | Hindi Message |
|---|---|---|
| **INVALID_DATA** | Invalid data provided. Please check your request. | अमान्य डेटा प्रदान किया गया। कृपया अपनी अनुरोध जाँचें। |
| **RESOURCE_NOT_FOUND** | Requested resource not found. | अनुरोधित संसाधन नहीं मिला। |
| **OPERATION_FAILED** | Operation failed. Please try again later. | ऑपरेशन विफल। कृपया बाद में पुनः प्रयास करें। |
| **TIMEOUT_OCCURRED** | Request timeout occurred. Please try again. | अनुरोध का समय समाप्त हो गया। |
| **CONFLICT_ERROR** | Conflict error. The resource already exists or has been modified. | संघर्ष त्रुटि। संसाधन पहले से मौजूद है या संशोधित किया गया है। |
| **INTERNAL_SERVER_ERROR** | An internal server error occurred. Please try again later. | सर्वर में आंतरिक त्रुटि हुई। कृपया बाद में पुनः प्रयास करें। |
| **SERVICE_UNAVAILABLE** | Service is temporarily unavailable. Please try again later. | सेवा अस्थायी रूप से अनुपलब्ध है। कृपया बाद में पुनः प्रयास करें। |
| **CONNECTION_FAILED** | Connection to the service failed. Please try again. | सेवा से कनेक्शन विफल। कृपया पुनः प्रयास करें। |
| **LANGUAGE_NOT_SUPPORT** | Language is not supported. | भाषा समर्थित नहीं है। |
| **DATABASE_OPERATION_FAILED** | Database operation failed. Please try again later. | डेटाबेस ऑपरेशन विफल। कृपया बाद में पुनः प्रयास करें। |
| **UNAUTHORIZED** | Unauthorized access. Please provide valid credentials. | अनुमति प्राप्त नहीं है। कृपया वैध क्रेडेंशियल प्रदान करें। |
| **AUTHENTICATION_FAILED** | Authentication failed. Invalid credentials provided. | प्रमाणीकरण विफल। अमान्य क्रेडेंशियल प्रदान किए गए हैं। |
| **VALIDATION_ERROR** | Validation error. Please check your input. | सत्यापन त्रुटि। कृपया अपना इनपुट जाँचें। |

## Key Components

### JwtTokenController
Handles HTTP requests for token generation, verification, and refresh operations. Processes User-Agent headers for tracking client information.

### JwtAuthenticationService
Core service responsible for:
- Generating JWT tokens with custom claims
- Verifying and validating tokens
- Handling token refresh logic
- Integrating with user agent information

### AuthenticationService
Base authentication service providing core authentication functionality.

### AESCryptography
Implements AES encryption/decryption for sensitive data protection.

### JwtTokenHelper
Utility class for JWT token creation, parsing, and validation operations.

### BycryptHasingUtil
Provides BCrypt-based password hashing and verification.

### UserAgentParser
Parses and extracts client information from User-Agent headers.

## Multi-Language Support

The service supports multiple languages through properties files:

- **messages.properties**: Default English messages
- **messages_en_IN.properties**: English (Indian locale) messages
- **messages_hi_IN.properties**: Hindi messages

Error messages are automatically resolved based on the configured locale.

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build
```bash
mvn clean build
```

### Run
```bash
mvn spring-boot:run
```

### Package as JAR
```bash
mvn clean package
```

The built JAR will be available at: `target/authentication-service-0.0.1.jar`

## Configuration

Configure the service through `application.properties`. The application uses Spring @Value annotations to inject configuration properties.

### Application Properties

#### Application Settings
```properties
spring.application.name=authentication-service
```

#### JWT Token Configuration
| Property | Default Value | Description |
|---|---|---|
| `jwt.secret` | `your_jwt_secret_key` | Secret key for JWT token signing |
| `mobile.token.expire.time.in.hours` | `168` | Mobile app token expiration time (hours) - Default 7 days |
| `web.token.expire.time.in.hours` | `2` | Web token expiration time (hours) - Default 2 hours |
| `refresh.token.expire.time.in.hours` | `720` | Refresh token expiration time (hours) - Default 30 days |

#### AES Encryption Configuration
| Property | Default Value | Description |
|---|---|---|
| `aes.encryption.password` | `your_aes_encryption_password` | Password for AES encryption/decryption |
| `aes.salt` | `9d7f1a3c5b8e2f44` | Salt value for AES encryption |

#### Configuration Example
```properties
# Application
spring.application.name=authentication-service

# JWT Configuration
jwt.secret=your_secure_jwt_secret_key_here
mobile.token.expire.time.in.hours=168
web.token.expire.time.in.hours=2
refresh.token.expire.time.in.hours=720

# AES Encryption
aes.encryption.password=your_secure_aes_password_here
aes.salt=9d7f1a3c5b8e2f44

# Optional: Spring Boot Server Configuration
server.port=8080
server.servlet.context-path=/auth-service

# Optional: Logging
logging.level.root=INFO
logging.level.com.common.oauth=DEBUG

# Optional: Locale
spring.web.locale=en_IN
```

### Property Injection Points

The following classes use @Value to inject configuration:

1. **JwtTokenHelper.java** (lines 34-37)
   - `mobile.token.expire.time.in.hours` (Default: 168 hours / 7 days)
   - `web.token.expire.time.in.hours` (Default: 2 hours)
   - `refresh.token.expire.time.in.hours` (Default: 720 hours / 30 days)
   - `jwt.secret` (Default: your_jwt_secret_key)

2. **TokenHelper.java** (lines 29-31)
   - `mobile.token.expire.time.in.hours` (Default: 1440 hours / 60 days)
   - `web.token.expire.time.in.hours` (Default: 8 hours)
   - `refresh.token.expire.time.in.hours` (Default: 1440 hours / 60 days)

3. **AESCryptography.java** (lines 24-25)
   - `aes.encryption.password` (Default: your_aes_encryption_password)
   - `aes.salt` (Default: 9d7f1a3c5b8e2f44)

### Security Notes

⚠️ **Important**: 
- Change `jwt.secret` to a strong, random secret in production
- Change `aes.encryption.password` to a secure password in production
- Store sensitive properties in environment variables or external configuration servers
- `application.properties` is typically gitignored for security reasons
- Use Spring Cloud Config or similar tools for managing secrets in microservices architecture

### Setting Properties

Properties can be configured in multiple ways (in order of precedence):
1. Command-line arguments: `java -jar app.jar --jwt.secret=value`
2. Environment variables: `export JWT_SECRET=value`
3. `application.properties` file
4. `application.yml` file
5. Default values in @Value annotations

## Development Notes

- **Java Version**: 17
- **Code Style**: Uses Lombok for clean code
- **Error Handling**: Centralized exception handling with ErrorCode enum
- **Message Resolution**: Property file based message resolution
- **Security**: BCrypt password hashing, AES encryption, JWT tokens

## Building with Maven

```bash
# Clean and build
mvn clean install

# Skip tests
mvn clean install -DskipTests

# Build specific module
mvn clean build -pl . -am
```

## Dependencies Management

All dependencies are managed through Maven's dependency management. See `pom.xml` for complete dependency list.

### Key Dependency Versions
- Spring Boot: 4.0.1
- Spring Cloud: 2022.0.4
- jbcrypt: 0.4
- JJWT: 0.11.5
- java-jwt: 3.18.2
- Spring Security Crypto: 5.7.2

## Support

For any issues or support, feel free to reach out via direct message (DM) or raise a pull request (PR).

---

**Last Updated**: December 2025

