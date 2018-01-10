-- phpMyAdmin SQL Dump
-- version 4.1.6
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jan 07, 2018 at 08:07 AM
-- Server version: 5.5.36
-- PHP Version: 5.4.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `ebook`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_news`
--

CREATE TABLE IF NOT EXISTS `tbl_news` (
  `nid` int(11) NOT NULL AUTO_INCREMENT,
  `news_heading` varchar(500) NOT NULL,
  `cat_id` int(11) NOT NULL,
  `news_status` int(11) NOT NULL DEFAULT '1',
  `news_date` varchar(255) NOT NULL,
  `news_image` text NOT NULL,
  `news_description` text NOT NULL,
  PRIMARY KEY (`nid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `tbl_news`
--

INSERT INTO `tbl_news` (`nid`, `news_heading`, `cat_id`, `news_status`, `news_date`, `news_image`, `news_description`) VALUES
(2, 'Tuhan, Haramkan Surga-Mu Bagiku', 2, 1, 'Cerita Cinta Abadi Rabi''ah al-Adawiyah', '', '<p>Rabi&#39;ah al-Adawiyah dianggap sebagai sufi perempuan yang telah berhasil menjelaskan konsep cinta ilahi.</p>\r\n'),
(3, 'IHYA'' ''ULUMUDDIN', 3, 1, 'Terjemahan Ringkas', '', '<p>Hidup Berada Diatas Jalan Syari&#39;at Islam</p>\r\n'),
(4, 'Laa Takhfa', 4, 1, 'Jangan Takut Gagal', '', '<p><strong>Sebab Allah bersama Orang Yang Berani Melangkah</strong></p>\r\n\r\n<p>Buku ini berisi motivasi dan langkah-langkah yang perlu kita ambil untuk mengubah diri kita menjadi pribadi yang baru dan lebih baik.</p>\r\n\r\n<p><em>&quot;kegagalan sesungguhnya hanya milik merekan yang takut melangkah.&quot;</em></p>\r\n'),
(5, 'Sukses Tanpa Batas', 8, 1, 'SERI Menbangun Potensi Diri 1', '', '<p>Apakah anda orang sukses? buku ini&nbsp; akan mengajak anda untuk melakukan perjalanan bersamanya menuju kehidupan dengan kesuksesan tanpa batas</p>\r\n'),
(6, 'Terlanjur Jadi MAHASISWA, mesti GILA', 6, 1, '-', '', '<p><em>Tidak peduli berapa jumlah IPK</em></p>\r\n\r\n<p><em>Tidak peduli jumlah SKS yang sudah diambil</em></p>\r\n\r\n<p><em>Tidak peduli sekarang anda semester berapa</em></p>\r\n\r\n<p><em>Jika anda masih bingung 5 tahun kedepan mau jadi apa</em></p>\r\n\r\n<p><em>maka. BANTING STIR sekarang juga!!</em></p>\r\n'),
(7, 'Butir-Butir Pemikiran', 7, 1, 'Menuju Pembaruan Gerakan Islam', '', '<p>Sayyid Quthb adalah tokoh pergerakan islam yang monumental dengan segenap kontroversinya.</p>\r\n'),
(8, 'Fiqih Praktis Madzhab Syafi''i', 5, 1, 'Kajian Fiqih Madzhab Terbesar di Indonesia', '', '<p>Buku ini merupakan terjemahan dari kitab <em>Matan Al-ghayat wa At-Taqrib</em> atau yang lebih di kenal dengan nama nama <em>Matan abi Syuja&#39; fi Al-fiqh Asy-Syafi&#39;i</em></p>\r\n');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_news_category`
--

CREATE TABLE IF NOT EXISTS `tbl_news_category` (
  `cid` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) NOT NULL,
  `category_image` text NOT NULL,
  `author` varchar(50) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`cid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `tbl_news_category`
--

INSERT INTO `tbl_news_category` (`cid`, `category_name`, `category_image`, `author`, `status`) VALUES
(2, 'History', '9446-2018-01-04.jpeg', 'Pengurus Majelis Dzikir dan Shalawat Walisongo', 1),
(3, 'IHYA'' ''ULUMUDDIN', '8319-2018-01-04.jpeg', 'AL-GHAZALI', 1),
(4, 'LAA TAKHFA', '3006-2018-01-07.jpeg', 'ALDILLA DHARMA', 1),
(5, 'FIQIH PRAKTIS', '6406-2018-01-07.jpeg', 'ABU SYUJA'' AL-ASHFAHANI', 1),
(6, 'Terlanjur Jadi MAHASISWA', '9828-2018-01-07.jpeg', 'Asul Fauzi', 1),
(7, 'Sayyid Quthb', '5361-2018-01-07.jpeg', 'K.Salim & Bahnasawi', 1),
(8, 'Sukses Tanpa Batas', '2764-2018-01-07.jpeg', 'Dr.Thariq Muhammas as suwaidan & Faishal Umar Basy', 1);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_user`
--

CREATE TABLE IF NOT EXISTS `tbl_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Username` varchar(15) NOT NULL,
  `Password` text NOT NULL,
  `Email` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `tbl_user`
--

INSERT INTO `tbl_user` (`ID`, `Username`, `Password`, `Email`) VALUES
(1, 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Ebookuas@gmail.com');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
