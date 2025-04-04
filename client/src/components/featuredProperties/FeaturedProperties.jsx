import useFetch from "../../hooks/useFetch";
import "./featuredProperties.css";
import { useNavigate } from "react-router-dom";

const FeaturedProperties = () => {
  const navigate = useNavigate();
  const { data, loading } = useFetch("/hotels/limit?limit=4");

  const handleClick = (hotelId) => {
    navigate(`/hotels/${hotelId}`);
  };

  return (
    <div className="fp">
      {loading ? (
        <div className="loading-text">Loading...</div>
      ) : (
        <>
          {data.map((item, index) => (
            <div
              className="fpItem"
              key={item.id || index}
              onClick={() => handleClick(item.id)}
              role="button"
              tabIndex={0}
            >
              <img
                src={item?.photos}
                alt={item.name}
                className="fpImg"
              />
              <div className="fpContent">
                <span className="fpName">{item.name}</span>
                <span className="fpCity">{item.city}</span>
                <span className="fpPrice">Giá từ ${item.cheapestPrice}</span>
                {item.rating && <div className="fpRating">
                  <button>{item.rating}</button>
                  <span>Excellent</span>
                </div>}
              </div>
            </div>
          ))}
        </>
      )}
    </div>
  );
};

export default FeaturedProperties;