import "./list.css";
import Navbar from "../../components/navbar/Navbar";
import Header from "../../components/header/Header";
import { useEffect, useState, useCallback } from "react";
import SearchItem from "../../components/searchItem/SearchItem";
import useFetch from "../../hooks/useFetch";
import { useSelector } from "react-redux";
import { message } from 'antd';
import MailList from "../../components/mailList/MailList";
import Footer from "../../components/footer/Footer";
import { GoogleMap, LoadScript, Marker } from '@react-google-maps/api';
import { KEY_GOOGLE_MAP } from "../../constants/googleMap/key";
import axios from 'axios';

const List = () => {
  // map
  const mapContainerStyle = {
    width: '100%',
    height: '250px',
  };

  const [mapCenter, setMapCenter] = useState({ lat: 16.0583, lng: 108.2772 }); // Đà Nẵng

  // toast error
  const [messageApi, contextHolder] = message.useMessage();

  // State quản lý phân trang
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(4);

  // Lấy state từ Redux
  const { city, dates, minPrice, maxPrice, name } = useSelector(state => state.search);

  // Tính toán phân trang
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;

  // Fetch dữ liệu
  const { data, loading, error } = useFetch(
    `/hotels/search?name=${name || ''}&city=${city || ''}&checkInDate=${dates[0]?.startDate || ''}&checkOutDate=${dates[0]?.endDate || ''}&minPrice=${minPrice || 0}&maxPrice=${maxPrice || 9999999}`
  );

  const currentItems = data?.slice(indexOfFirstItem, indexOfLastItem) || [];
  const totalPages = Math.ceil((data?.length || 0) / itemsPerPage);

  const errorSearch = useCallback((message) => {
    messageApi.open({
      type: 'error',
      content: message,
    });
  }, [messageApi]);

  useEffect(() => {
    if (error) {
      errorSearch(error.response?.data?.errors?.[0] || 'Error fetching data');
    }
  }, [error, errorSearch, data])

  useEffect(() => {
    const fetchMapCenter = async () => {
      try {
        const response = await axios.get(`https://maps.googleapis.com/maps/api/geocode/json?address=${city}&key=${KEY_GOOGLE_MAP}`);
        const { lat, lng } = response.data.results[0].geometry.location;
        setMapCenter({ lat, lng });
      } catch (error) {
        console.error('Error fetching map center:', error);
      }
    };

    if (city) {
      fetchMapCenter();
    }
  }, [city]);

  return (
    <div>
      {contextHolder}
      <Navbar />
      <Header type="list" />
      <div className="listContainer">
        <div className="listWrapper">
          <div className="listSearch">
            <h1 className="lsTitle">Map</h1>
            <LoadScript googleMapsApiKey={KEY_GOOGLE_MAP}>
              <GoogleMap
                className="bright-map"
                mapContainerStyle={mapContainerStyle}
                center={mapCenter}
                zoom={12}
                options={{
                  styles: [
                    {
                      featureType: 'all',
                      elementType: 'all',
                      stylers: [
                        { saturation: -100 },
                        { lightness: 50 },
                      ],
                    },
                  ],
                }}
              >
                <Marker position={mapCenter} />
              </GoogleMap>
            </LoadScript>
          </div>

          <div className="listResult">
            {loading ? (
              "Đang tải..."
            ) : (
              <>
                {/* Hiển thị kết quả phân trang */}
                {currentItems.map((item, index) => (
                  <SearchItem item={item} key={item.id || index} />
                ))}

                {/* Phân trang */}
                {data?.length > 0 && (
                  <div className="pagination">
                    <button
                      onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                      disabled={currentPage === 1}
                    >
                      Before
                    </button>

                    {Array.from({ length: totalPages }, (_, i) => (
                      <button
                        key={i + 1}
                        onClick={() => setCurrentPage(i + 1)}
                        className={currentPage === i + 1 ? "active" : ""}
                      >
                        {i + 1}
                      </button>
                    ))}

                    <button
                      onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
                      disabled={currentPage === totalPages}
                    >
                      After
                    </button>
                  </div>
                )}
              </>
            )}
          </div>
        </div>
      </div>
      <MailList />
      <Footer />
    </div>
  );
};

export default List;
