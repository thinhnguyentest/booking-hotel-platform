import "./single.scss";
import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";
import Chart from "../../components/chart/Chart";
import List from "../../components/table/Table";
import useFetch from "../../hooks/useFetch";
import { useLocation } from "react-router-dom";

const SingleRoom = () => {
  const location = useLocation();
  const roomId = location.pathname.split("/").pop();
  const { data, loading, error } = useFetch(`/rooms/${roomId}`);
  console.log('roomId >> ', roomId)
  console.log('data >> ', data)

  return (
    <div className="single">
      <Sidebar />
      <div className="singleContainer">
        <Navbar />
        <div className="top">
          <div className="left">
            <div className="editButton">Edit</div>
            <h1 className="title">Room Information</h1>
            {loading ? (
              <p>Loading...</p>
            ) : error ? (
              <p>Error fetching data</p>
            ) : (
              <div className="item">
                <img
                  src={
                    data?.hotelResponse?.photos ||
                    "https://via.placeholder.com/150"
                  }
                  alt="Hotel"
                  className="itemImg"
                />
                <div className="details">
                  <h1 className="itemTitle">{data.title}</h1>
                  <div className="detailItem">
                    <span className="itemKey">Price:</span>
                    <span className="itemValue">${data?.price}</span>
                  </div>
                  <div className="detailItem">
                    <span className="itemKey">Room Type:</span>
                    <span className="itemValue">{data?.roomType}</span>
                  </div>
                  <div className="detailItem">
                    <span className="itemKey">Room Number:</span>
                    <span className="itemValue">{data?.roomNumber}</span>
                  </div>
                  <div className="detailItem">
                    <span className="itemKey">Hotel:</span>
                    <span className="itemValue">{data?.hotelResponse?.name}</span>
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

export default SingleRoom;
