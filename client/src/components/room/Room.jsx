import { faCircleArrowLeft, faCircleArrowRight, faCircleXmark, faLocationDot, faStar } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import React, { useCallback, useEffect, useState } from 'react'
import useFetch from '../../hooks/useFetch';
import { useSelector } from 'react-redux';
import { parseEncodedDate } from '../../utils/dateUtils/parseEncodeTimeZoned';
import { decodeURIComponent } from '../../utils/dateUtils/formatTimeZoned';
import { message } from 'antd';
import axios from 'axios';

const Room = ({ room }) => {
  const [slideNumber, setSlideNumber] = useState(0);
  const [messageApi, contextHolder] = message.useMessage();
  const [error, setError] = useState(null);
  const [open, setOpen] = useState(false);
  const { data: dataImg, loading } = useFetch(`/images/imageByRoom?roomId=${room?.roomId || 5}`)

  const errorSearch = useCallback((message) => {
    messageApi.open({
      type: 'error',
      content: message,
    });
  }, [messageApi]);

  useEffect(() => {
    if (error) {
      errorSearch(error);
    }
  }, [error, errorSearch]);

  const handleOpen = (i) => {
    setSlideNumber(i);
    setOpen(true);
  };

  const { dates } = useSelector(state => state.search);

  const MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
  const dayDifference = (date1, date2) => {
    const timeDiff = Math.abs(date2.getTime() - date1.getTime());
    const diffDays = Math.ceil(timeDiff / MILLISECONDS_PER_DAY);
    return diffDays;
  }

  const startDate = parseEncodedDate(dates[0]?.startDate);
  const endDate = parseEncodedDate(dates[0]?.endDate);
  const days = dayDifference(endDate, startDate);
  const handleCreateBooking = async () => {
    try {
      const resBooking = await axios.post(`/bookings?roomId=${room?.roomId}`, {
        checkInDate: decodeURIComponent(dates[0]?.startDate),
        checkOutDate: decodeURIComponent(dates[0]?.endDate),
        totalPrice: (days || 1) * (room?.price)
      })
      const newBooking = resBooking?.data;
      const resPayment = await axios.post(`/payments/checkout?bookingId=${newBooking?.bookingId}`);
      const paymentUrl = resPayment?.data?.sessionUrl;
      if (paymentUrl) {
        window.location.href = paymentUrl;
      }
      setError('');
    } catch (error) {
      setError(error.response?.data?.errors?.[0] || 'Please select check-in and check-out date');
    }
  }

  const handleMove = (direction) => {
    let newSlideNumber;

    if (direction === "l") {
      newSlideNumber = slideNumber === 0 ? 5 : slideNumber - 1;
    } else {
      newSlideNumber = slideNumber === 5 ? 0 : slideNumber + 1;
    }

    setSlideNumber(newSlideNumber)
  };
  return (
    <div>
      {contextHolder}
      {loading ? "Loading..." : <div className="hotelContainer">
        {open && (
          <div className="slider">
            <FontAwesomeIcon
              icon={faCircleXmark}
              className="close"
              onClick={() => setOpen(false)}
            />
            <FontAwesomeIcon
              icon={faCircleArrowLeft}
              className="arrow"
              onClick={() => handleMove("l")}
            />
            <div className="sliderWrapper">
              <img src={dataImg[slideNumber]?.imageUrl} alt="" className="sliderImg" />
            </div>
            <FontAwesomeIcon
              icon={faCircleArrowRight}
              className="arrow"
              onClick={() => handleMove("r")}
            />
          </div>
        )}
        <div className="hotelWrapper">
          <h1 className="hotelTitle">{room?.roomType || 'Luxury room'}</h1>
          <div className="hotelAddress">
            <FontAwesomeIcon icon={faLocationDot} />
            <span>{room?.hotelResponse?.address}</span>
          </div>
          <span className="hotelDistance">
            Excellent location – {room?.hotelResponse?.city}
          </span>
          <span className="hotelPriceHighlight">
            Book a stay over ${room?.price} at this property and get a free airport taxi
          </span>
          <div className="hotelImages">
            {dataImg.map((photo, i) => (
              <div className="hotelImgWrapper" key={i}>
                <img
                  onClick={() => handleOpen(i)}
                  src={photo.imageUrl}
                  alt=""
                  className="hotelImg"
                />
              </div>
            ))}
          </div>
          <div className="hotelDetails">
            <div className="hotelDetailsTexts">
              <h1 className="hotelTitle">Stay in the heart of City</h1>
              <p className="hotelDesc">
                Located a 5-minute walk from St. Florian's Gate in Krakow, Tower
                Street Apartments has accommodations with air conditioning and
                free WiFi. The units come with hardwood floors and feature a
                fully equipped kitchenette with a microwave, a flat-screen TV,
                and a private bathroom with shower and a hairdryer. A fridge is
                also offered, as well as an electric tea pot and a coffee
                machine. Popular points of interest near the apartment include
                Cloth Hall, Main Market Square and Town Hall Tower. The nearest
                airport is John Paul II International Kraków–Balice, 16.1 km
                from Tower Street Apartments, and the property offers a paid
                airport shuttle service.
              </p>
            </div>
            <div className="hotelDetailsPrice">
              <div className="priceHeader">
                <h2 className="pricePerNight">${room?.price} /night</h2>
                <div className="reviewRating">
                  <span className="ratingValue">{room?.hotelResponse?.rating || 5.0} <FontAwesomeIcon icon={faStar} /></span>
                  <span className="ratingText">(122 reviews)</span>
                </div>
              </div>

              <div className="dateSection">
                {dates[0]?.startDate ? <div className="dateRow">
                  <span>Check in</span>
                  <span className="dateValue">
                    {startDate?.toDateString('en-US', { month: 'short', day: 'numeric' })}
                  </span>
                </div>
                :<>
                  <span>Check in</span>
                  <hr />
                </>
                }
                {dates[0]?.endDate ? <div className="dateRow">
                  <span>Check out</span>
                  <span className="dateValue">
                    {endDate?.toDateString('en-US', { month: 'short', day: 'numeric' })}
                  </span>
                </div>
                : <span>Check out</span>}
              </div>
              <div className="priceBreakdown">
                <div className="priceRow">
                  <span>${room?.price * (days || 1)} / {days || 1} night</span>
                  <span>${(days || 1) * (room?.price)}</span> 
                </div>
              </div>

              <div className="totalPrice">
                <span>Total</span>
                <span className="totalAmount">${(days || 1) * (room?.price)}</span>
              </div>

              <button className="reserveButton" onClick={handleCreateBooking}>
                Reserve
              </button>
            </div>

          </div>
        </div>
      </div>}
    </div>
  )
}

export default Room;
