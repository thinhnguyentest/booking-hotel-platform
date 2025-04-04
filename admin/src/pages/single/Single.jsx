import "./single.scss";
import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";
import Chart from "../../components/chart/Chart";
import List from "../../components/table/Table";
import useFetch from "../../hooks/useFetch";
import { useLocation } from "react-router-dom";

const Single = () => {
  const location = useLocation();
  const userId = location.pathname.split("/").pop();
  const { data, loading, error } = useFetch(`/users/${userId}`);
  
  return (
    <div className="single">
      <Sidebar />
      <div className="singleContainer">
        <Navbar />
        <div className="top">
          <div className="left">
            <div className="editButton">Edit</div>
            <h1 className="title">Information</h1>
            {loading ? (
              <p>Loading...</p>
            ) : error ? (
              <p>Error fetching data</p>
            ) : (
              <div className="item">
                <img
                  src={
                    data.img ||
                    "https://static.vecteezy.com/system/resources/previews/036/280/651/non_2x/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-illustration-vector.jpg"
                  }
                  alt=""
                  className="itemImg"
                />
                <div className="details">
                  <h1 className="itemTitle">{data?.username}</h1>
                  <div className="detailItem">
                    <span className="itemKey">Id:</span>
                    <span className="itemValue">{data?.userId}</span>
                  </div>
                  <div className="detailItem">
                    <span className="itemKey">Email:</span>
                    <span className="itemValue">{data?.email}</span>
                  </div>
                  <div className="detailItem">
                    <span className="itemKey">Country:</span>
                    <span className="itemValue">Viet Nam</span>
                  </div>
                </div>
              </div>
            )}
          </div>
          <div className="right">
            <Chart aspect={3 / 1} title="User Spending ( Last 6 Months)" />
          </div>
        </div>
        <div className="bottom">
          <h1 className="title">Last Transactions</h1>
          <List />
        </div>
      </div>
    </div>
  );
};

export default Single;
