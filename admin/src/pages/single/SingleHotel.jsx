import "./single.scss";
import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";
import Chart from "../../components/chart/Chart";
import List from "../../components/table/Table";
import useFetch from "../../hooks/useFetch";
import { useLocation } from "react-router-dom";

const SingleHotel = () => {
  const location = useLocation();
  const hotelId = location.pathname.split("/").pop();
  const { data, loading, error } = useFetch(`/hotels/${hotelId}`);
  console.log('hotelId >> ', hotelId)
  console.log('data >> ',data)
  console.log('loading >> ',loading)
  console.log('error >> ',error)
  return (
    <div className="single">
      <Sidebar />
      <div className="singleContainer">
        <Navbar />
        <div className="top">
          <div className="left">
            <div className="editButton">Edit</div>
            <h1 className="title">Hotel Information</h1>
            {loading ? (
              <p>Loading...</p>
            ) : error ? (
              <p>Error fetching data</p>
            ) : (
              <div className="item">
                <img
                  src={
                    data?.photos ||
                    "https://via.placeholder.com/150"
                  }
                  alt="Hotel"
                  className="itemImg"
                />
                <div className="details">
                  <h1 className="itemTitle">{data?.name}</h1>
                  <div className="detailItem">
                    <span className="itemKey">Country:</span>
                    <span className="itemValue">{data?.country}</span>
                  </div>
                  <div className="detailItem">
                    <span className="itemKey">City:</span>
                    <span className="itemValue">{data.city}</span>
                  </div>
                  <div className="detailItem">
                    <span className="itemKey">Address:</span>
                    <span className="itemValue">{data.address}</span>
                  </div>
                  <div className="detailItem">
                    <span className="itemKey">Price:</span>
                    <span className="itemValue">${data.cheapestPrice}</span>
                  </div>
                  <div className="detailItem">
                    <span className="itemKey">Featured:</span>
                    <span className="itemValue">{data.featured ? "Yes" : "No"}</span>
                  </div>
                </div>
              </div>
            )}
          </div>
          <div className="right">
            <Chart aspect={3 / 1} title="Booking Trends (Last 6 Months)" />
          </div>
        </div>
        <List/>
      </div>
    </div>
  );
};

export default SingleHotel;
