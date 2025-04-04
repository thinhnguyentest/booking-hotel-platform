import "./newVoucher.scss";
import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";
import { useState } from "react";
import { voucherInputs } from "../../formSource"; // Đổi thành voucherInputs
import useFetch from "../../hooks/useFetch";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const NewVoucher = () => {
  const [info, setInfo] = useState({});
  const [applicableProducts, setApplicableProducts] = useState(""); // Thêm trường sản phẩm áp dụng
  const navigate = useNavigate();

  const handleChange = (e) => {
    setInfo((prev) => ({ ...prev, [e.target.id]: e.target.value }));
  };

  const handleClick = async (e) => {
    e.preventDefault();
    try {
      // Chuyển applicableProducts thành mảng
      const productsArray = applicableProducts.split(',').map(item => item.trim());
      
      await axios.post(`/vouchers`, {
        ...info,
        applicableProducts: productsArray,
        isActive: true // Thêm trạng thái mặc định
      });
      navigate("/vouchers");
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <div className="new">
      <Sidebar />
      <div className="newContainer">
        <Navbar />
        <div className="top">
          <h1>Add New Voucher</h1>
        </div>
        <div className="bottom">
          <div className="right">
            <form>
              {voucherInputs.map((input) => (
                <div className="formInput" key={input.id}>
                  <label>{input.label}</label>
                  {input.type === "select" ? (
                    <select
                      id={input.id}
                      onChange={handleChange}
                    >
                      {input.options.map(option => (
                        <option key={option.value} value={option.value}>
                          {option.label}
                        </option>
                      ))}
                    </select>
                  ) : (
                    <input
                      id={input.id}
                      type={input.type}
                      placeholder={input.placeholder}
                      onChange={handleChange}
                      step={input.type === "number" ? "0.01" : undefined}
                    />
                  )}
                </div>
              ))}
              
              {/* Trường nhập sản phẩm áp dụng */}
              <div className="formInput">
                <label>Applicable Products</label>
                <input
                  type="text"
                  placeholder="Enter product IDs, separated by commas"
                  value={applicableProducts}
                  onChange={(e) => setApplicableProducts(e.target.value)}
                />
              </div>

              <button onClick={handleClick}>Create Voucher</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NewVoucher; // Đổi tên export
