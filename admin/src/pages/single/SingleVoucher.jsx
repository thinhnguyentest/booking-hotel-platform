import "./single.scss";
import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";
import Chart from "../../components/chart/Chart";
import List from "../../components/table/Table";
import useFetch from "../../hooks/useFetch";
import { useLocation } from "react-router-dom";
import { format } from 'date-fns';

const SingleVoucher = () => {
  const location = useLocation();
  const voucherId = location.pathname.split("/").pop();
  console.log('voucherId >> ', voucherId)
  const { data, loading, error } = useFetch(`/vouchers/${voucherId}`);
  console.log('data >> ',data )

  const formatDate = (dateString) => {
    try {
      const date = new Date(dateString);
      return format(date, 'dd/MM/yyyy');
    } catch (error) {
      return 'Invalid date';
    }
  };

  return (
    <div className="single">
      <Sidebar />
      <div className="singleContainer">
        <Navbar />
        <div className="top">
          <div className="left">
            <div className="editButton">Edit</div>
            <h1 className="title">Voucher Information</h1>
            {loading ? (
              <p>Loading...</p>
            ) : error ? (
              <p>Error fetching data</p>
            ) : (
              <div className="item">
                <img
                  src={'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ4FiLYzwUSUoXUySbSj3oxIMgScLK-ZrHhDg&s'}
                  alt="Voucher"
                  className="itemImg"
                />
                <div className="details">
                  <h1 className="itemTitle">{data.code}</h1>
                  <div className="detailItem">
                    <span className="itemKey">Discount:</span>
                    <span className="itemValue">${data?.discount}</span>
                  </div>
                  <div className="detailItem">
                    <span className="itemKey">Start Date:</span>
                    <span className="itemValue">{data?.startDate ? formatDate(data.startDate) : 'N/A'}</span>
                  </div>
                  <div className="detailItem">
                    <span className="itemKey">End Date:</span>
                    <span className="itemValue">{data?.endDate ? formatDate(data.endDate) : 'N/A'}</span>
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

export default SingleVoucher;
