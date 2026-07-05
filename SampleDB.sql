-- Tạo database SampleDB
CREATE DATABASE SampleDB;
GO

USE SampleDB;
GO

-- Tạo bảng Registration
CREATE TABLE Registration (
    UserName  VARCHAR(30)   NOT NULL PRIMARY KEY,
    Password  VARCHAR(30)   NOT NULL,
    LastName  NVARCHAR(50)  NOT NULL,
    isAdmin   BIT           NOT NULL
);
GO

-- Chèn dữ liệu mẫu
INSERT INTO Registration (UserName, Password, LastName, isAdmin) VALUES
('U001', '123', 'Tom',   1),
('U002', '456', 'David', 0),
('U003', '789', 'John',  0),
('U004', '012', 'Mark',  1),
('U005', '134', 'Kate',  0);
GO
