-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:4306
-- Generation Time: Dec 04, 2024 at 01:01 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `food_database`
--

-- --------------------------------------------------------

--
-- Table structure for table `kategori`
--

CREATE TABLE `kategori` (
  `id_kategori` varchar(3) NOT NULL,
  `nama_kategori` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kategori`
--

INSERT INTO `kategori` (`id_kategori`, `nama_kategori`) VALUES
('k01', 'buah'),
('k02', 'sayur'),
('k03', 'rempah'),
('k04', 'protein');

-- --------------------------------------------------------

--
-- Table structure for table `makanan`
--

CREATE TABLE `makanan` (
  `id_makanan` int(11) NOT NULL,
  `nama_makanan` varchar(50) NOT NULL,
  `kategori_id` varchar(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `makanan`
--

INSERT INTO `makanan` (`id_makanan`, `nama_makanan`, `kategori_id`) VALUES
(1, 'anggur', 'k01'),
(2, 'apel', 'k01'),
(3, 'ayam', 'k04'),
(4, 'bawang', 'k02'),
(5, 'bayam', 'k02'),
(6, 'belimbing', 'k01'),
(7, 'bombay', 'k02'),
(8, 'brokoli', 'k02'),
(9, 'buah_bit', 'k01'),
(10, 'cabai_rawit', 'k03'),
(11, 'delima', 'k01'),
(12, 'ikan', 'k04'),
(13, 'jagung', 'k02'),
(14, 'jagung_manis', 'k02'),
(15, 'jahe', 'k03'),
(16, 'jalepeno', 'k03'),
(17, 'jeruk', 'k01'),
(18, 'kacang_kedelai', 'k04'),
(19, 'kacang_polong', 'k04'),
(20, 'kangkung', 'k02'),
(21, 'kapsikum', 'k02'),
(22, 'kelapa', 'k01'),
(23, 'kembang_kol', 'k02'),
(24, 'kentang', 'k02'),
(25, 'kiwi', 'k01'),
(26, 'kubis', 'k02'),
(27, 'lemon', 'k01'),
(28, 'lobak', 'k02'),
(29, 'mangga', 'k01'),
(30, 'nanas', 'k01'),
(31, 'paprika', 'k02'),
(32, 'pare', 'k02'),
(33, 'pir', 'k01'),
(34, 'pisang', 'k01'),
(35, 'pokcoy', 'k02'),
(36, 'red_paprika', 'k02'),
(37, 'sapi', 'k04'),
(38, 'selada', 'k02'),
(39, 'semangka', 'k01'),
(40, 'singkong', 'k02'),
(41, 'tahu', 'k04'),
(42, 'telur', 'k04'),
(43, 'tempe', 'k04'),
(44, 'terong', 'k02'),
(45, 'timun', 'k02'),
(46, 'tomat', 'k02'),
(47, 'ubi_jalar', 'k02'),
(48, 'ubi_lobak', 'k02'),
(49, 'udang', 'k04'),
(50, 'wortel', 'k02');

-- --------------------------------------------------------

--
-- Table structure for table `nutrisi`
--

CREATE TABLE `nutrisi` (
  `id_nutrisi` varchar(5) NOT NULL,
  `makanan_id` int(11) DEFAULT NULL,
  `kategori_id` varchar(5) DEFAULT NULL,
  `kalori` decimal(5,2) DEFAULT NULL,
  `protein` decimal(5,2) DEFAULT NULL,
  `lemak` decimal(5,2) DEFAULT NULL,
  `karbohidrat` decimal(5,2) DEFAULT NULL,
  `serat` decimal(5,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nutrisi`
--

INSERT INTO `nutrisi` (`id_nutrisi`, `makanan_id`, `kategori_id`, `kalori`, `protein`, `lemak`, `karbohidrat`, `serat`) VALUES
('n01', 1, 'k01', 50.00, 0.00, 0.00, 12.00, 1.00),
('n02', 2, 'k01', 52.00, 0.00, 0.00, 14.00, 2.00),
('n03', 3, 'k04', 165.00, 31.00, 3.60, 0.00, 0.00),
('n04', 4, 'k02', 40.00, 1.00, 0.10, 9.00, 1.00),
('n05', 5, 'k02', 23.00, 2.90, 0.40, 3.60, 2.00),
('n06', 6, 'k01', 43.00, 0.00, 0.00, 11.00, 2.00),
('n07', 7, 'k02', 40.00, 1.30, 0.00, 9.30, 2.00),
('n08', 8, 'k02', 34.00, 3.70, 0.40, 6.60, 2.00),
('n09', 9, 'k01', 43.00, 1.60, 0.20, 10.00, 3.00),
('n10', 10, 'k03', 40.00, 0.90, 0.40, 9.50, 1.00),
('n11', 11, 'k01', 83.00, 1.30, 0.20, 22.00, 3.00),
('n12', 12, 'k04', 143.00, 19.80, 5.40, 0.00, 0.00),
('n13', 13, 'k02', 96.00, 3.40, 1.50, 21.00, 2.00),
('n14', 14, 'k02', 86.00, 3.20, 1.00, 19.00, 2.00),
('n15', 15, 'k03', 80.00, 1.80, 0.20, 18.00, 3.00),
('n16', 16, 'k03', 40.00, 0.90, 0.40, 9.30, 1.00),
('n17', 17, 'k01', 43.00, 1.00, 0.40, 11.00, 2.00),
('n18', 18, 'k04', 446.00, 37.50, 19.90, 30.20, 15.00),
('n19', 19, 'k02', 350.00, 21.00, 1.50, 60.00, 15.00),
('n20', 20, 'k02', 23.00, 2.60, 0.30, 4.10, 2.00),
('n21', 21, 'k02', 33.00, 0.30, 0.30, 8.60, 4.00),
('n22', 22, 'k01', 360.00, 3.20, 33.50, 11.90, 9.00),
('n23', 23, 'k02', 25.00, 1.50, 0.10, 5.40, 1.00),
('n24', 24, 'k02', 77.00, 2.10, 0.10, 17.50, 2.50),
('n25', 25, 'k01', 42.00, 0.90, 0.10, 11.00, 3.00),
('n26', 26, 'k02', 25.00, 0.60, 0.20, 6.10, 3.00),
('n27', 27, 'k01', 29.00, 1.10, 0.10, 7.30, 2.00),
('n28', 28, 'k02', 40.00, 1.00, 0.10, 9.40, 2.00),
('n29', 29, 'k01', 60.00, 1.00, 0.10, 15.00, 2.00),
('n30', 30, 'k01', 50.00, 0.50, 0.10, 13.00, 1.00),
('n31', 31, 'k01', 30.00, 1.00, 0.30, 6.60, 1.00),
('n32', 32, 'k02', 20.00, 1.60, 0.40, 4.60, 2.00),
('n33', 33, 'k01', 57.00, 0.30, 0.30, 14.70, 2.00),
('n34', 34, 'k01', 89.00, 1.10, 0.30, 23.00, 2.00),
('n35', 35, 'k02', 20.00, 1.60, 0.30, 4.40, 2.00),
('n36', 36, 'k01', 30.00, 1.10, 0.40, 6.50, 1.00),
('n37', 37, 'k04', 250.00, 26.20, 17.00, 0.00, 0.00),
('n38', 38, 'k02', 15.00, 1.50, 0.10, 2.90, 1.00),
('n39', 39, 'k01', 30.00, 0.80, 0.20, 7.50, 1.00),
('n40', 40, 'k02', 112.00, 1.60, 0.20, 28.30, 3.00),
('n41', 41, 'k04', 144.00, 15.00, 8.00, 0.00, 0.00),
('n42', 42, 'k04', 155.00, 13.00, 11.00, 0.00, 0.00),
('n43', 43, 'k04', 191.00, 17.80, 11.70, 0.00, 0.00),
('n44', 44, 'k02', 25.00, 0.90, 0.10, 5.80, 2.00),
('n45', 45, 'k02', 16.00, 0.70, 0.10, 3.60, 1.00),
('n46', 46, 'k01', 18.00, 1.00, 0.10, 4.00, 2.00),
('n47', 47, 'k02', 112.00, 1.50, 0.30, 27.60, 4.00),
('n48', 48, 'k02', 61.00, 0.80, 0.20, 15.70, 3.00),
('n49', 49, 'k04', 70.00, 15.50, 0.50, 1.00, 0.00),
('n50', 50, 'k02', 41.00, 0.90, 0.20, 9.60, 2.00);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `kategori`
--
ALTER TABLE `kategori`
  ADD PRIMARY KEY (`id_kategori`);

--
-- Indexes for table `makanan`
--
ALTER TABLE `makanan`
  ADD PRIMARY KEY (`id_makanan`),
  ADD KEY `kategori_id` (`kategori_id`);

--
-- Indexes for table `nutrisi`
--
ALTER TABLE `nutrisi`
  ADD PRIMARY KEY (`id_nutrisi`),
  ADD KEY `makanan_id` (`makanan_id`),
  ADD KEY `kategori_id` (`kategori_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `makanan`
--
ALTER TABLE `makanan`
  MODIFY `id_makanan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `makanan`
--
ALTER TABLE `makanan`
  ADD CONSTRAINT `makanan_ibfk_1` FOREIGN KEY (`kategori_id`) REFERENCES `kategori` (`id_kategori`);

--
-- Constraints for table `nutrisi`
--
ALTER TABLE `nutrisi`
  ADD CONSTRAINT `nutrisi_ibfk_1` FOREIGN KEY (`makanan_id`) REFERENCES `makanan` (`id_makanan`),
  ADD CONSTRAINT `nutrisi_ibfk_2` FOREIGN KEY (`kategori_id`) REFERENCES `kategori` (`id_kategori`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
