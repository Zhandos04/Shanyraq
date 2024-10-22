-- Step 1: Create the Database
CREATE DATABASE turamyzba;

-- Step 2: Connect to the Database
\c turamyzba;

-- Step 3: Create Schema
CREATE SCHEMA IF NOT EXISTS turamyzba;

-- Step 4: Create Tables with IF NOT EXISTS Constraints

-- User Table
CREATE TABLE IF NOT EXISTS turamyzba."User"
(
    ID        SERIAL PRIMARY KEY,
    firstName VARCHAR(50),
    lastName  VARCHAR(50),
    info      TEXT,
    gender    VARCHAR(10),
    email     VARCHAR(100) UNIQUE,
    password  VARCHAR(255),
    roles     VARCHAR(100),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    isDeleted BOOLEAN   DEFAULT FALSE
    );


-- Phone Table
CREATE TABLE IF NOT EXISTS turamyzba.Phone
(
    ID          SERIAL PRIMARY KEY,
    userId      INT,
    phoneNumber VARCHAR(15) UNIQUE,
    isVerify    BOOLEAN   DEFAULT FALSE,
    createdAt   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    isDeleted   BOOLEAN   DEFAULT FALSE,
    CONSTRAINT FK_Phone_User FOREIGN KEY (userId) REFERENCES turamyzba."User" (ID)
    );

-- WhatsApp Table
CREATE TABLE IF NOT EXISTS turamyzba.WhatsApp
(
    ID          SERIAL PRIMARY KEY,
    userId      INT,
    phoneNumber VARCHAR(15) UNIQUE,
    isVerify    BOOLEAN   DEFAULT FALSE,
    createdAt   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    isDeleted   BOOLEAN   DEFAULT FALSE,
    CONSTRAINT FK_WhatsApp_User FOREIGN KEY (userId) REFERENCES turamyzba."User" (ID)
    );


-- Announcement Table
CREATE TABLE IF NOT EXISTS turamyzba.Announcement
(
    ID                       SERIAL PRIMARY KEY,
    userId                   INT,
    title                    VARCHAR(255),
    apartmentsInfo           TEXT,
    address                  VARCHAR(255),
    coords                   VARCHAR(100),
    deposit                  DECIMAL(10, 2),
    maxPeople                INT,
    selectedGender           VARCHAR(10),
    isCommunalServiceInclude BOOLEAN   DEFAULT FALSE,
    roomiePreferences        TEXT,
    monthlyExpensePerPerson  DECIMAL(10, 2),
    status                   VARCHAR(50),
    startAt                  TIMESTAMP,
    createdAt                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    isDeleted                BOOLEAN   DEFAULT FALSE,
    CONSTRAINT FK_Announcement_User FOREIGN KEY (userId) REFERENCES turamyzba."User" (ID)
    );


-- AnnouncementUser Table
CREATE TABLE IF NOT EXISTS turamyzba.AnnouncementUser
(
    ID             SERIAL PRIMARY KEY,
    announcementId INT,
    userId         INT,
    isActive       BOOLEAN   DEFAULT TRUE,
    createdAt      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    isDeleted      BOOLEAN   DEFAULT FALSE,
    CONSTRAINT FK_AnnouncementUser_Announcement FOREIGN KEY (announcementId) REFERENCES turamyzba.Announcement (ID),
    CONSTRAINT FK_AnnouncementUser_User FOREIGN KEY (userId) REFERENCES turamyzba."User" (ID)
    );


-- AnnouncementWhatsApp Table
CREATE TABLE IF NOT EXISTS turamyzba.AnnouncementWhatsApp
(
    ID             SERIAL PRIMARY KEY,
    announcementId INT,
    whatsappId     INT,
    createdAt      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    isDeleted      BOOLEAN   DEFAULT FALSE,
    CONSTRAINT FK_AnnouncementWhatsApp_Announcement FOREIGN KEY (announcementId) REFERENCES turamyzba.Announcement (ID),
    CONSTRAINT FK_AnnouncementWhatsApp_WhatsApp FOREIGN KEY (whatsappId) REFERENCES turamyzba.WhatsApp (ID)
    );


-- AnnouncementPhone Table
CREATE TABLE IF NOT EXISTS turamyzba.AnnouncementPhone
(
    ID             SERIAL PRIMARY KEY,
    announcementId INT,
    phoneId        INT,
    createdAt      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    isDeleted      BOOLEAN   DEFAULT FALSE,
    CONSTRAINT FK_AnnouncementPhone_Announcement FOREIGN KEY (announcementId) REFERENCES turamyzba.Announcement (ID),
    CONSTRAINT FK_AnnouncementPhone_Phone FOREIGN KEY (phoneId) REFERENCES turamyzba.Phone (ID)
    );


-- Review Table
CREATE TABLE IF NOT EXISTS turamyzba.Review
(
    ID          SERIAL PRIMARY KEY,
    createdBy   INT,
    addressedTo INT,
    content     TEXT,
    createdAt   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    isDeleted   BOOLEAN   DEFAULT FALSE,
    CONSTRAINT FK_Review_CreatedBy FOREIGN KEY (createdBy) REFERENCES turamyzba."User" (ID),
    CONSTRAINT FK_Review_AddressedTo FOREIGN KEY (addressedTo) REFERENCES turamyzba."User" (ID)
    );


-- ReviewVotes Table
CREATE TABLE IF NOT EXISTS turamyzba.ReviewVotes
(
    ID        SERIAL PRIMARY KEY,
    reviewId  INT,
    userId    INT,
    isUp      BOOLEAN,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    isDeleted BOOLEAN   DEFAULT FALSE,
    CONSTRAINT FK_ReviewVotes_Review FOREIGN KEY (reviewId) REFERENCES turamyzba.Review (ID),
    CONSTRAINT FK_ReviewVotes_User FOREIGN KEY (userId) REFERENCES turamyzba."User" (ID)
    );
