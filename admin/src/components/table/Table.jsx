import "./table.scss";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import useFetch from "../../hooks/useFetch";
// import { useLocation } from "react-router-dom";

const List = () => {
  // const location = useLocation();
  // const userId = location.pathname.split("/").pop();
  const { data, loading, error } = useFetch(`/bookings/booking-by-userId/1`);

  return (
    <TableContainer component={Paper} className="table">
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell className="tableCell">Booking ID</TableCell>
            <TableCell className="tableCell">Hotel</TableCell>
            <TableCell className="tableCell">Room Number</TableCell>
            <TableCell className="tableCell">Check-in</TableCell>
            <TableCell className="tableCell">Check-out</TableCell>
            <TableCell className="tableCell">Total Price</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {loading ? (
            <TableRow>
              <TableCell colSpan={7} className="tableCell">Loading...</TableCell>
            </TableRow>
          ) : error ? (
            <TableRow>
              <TableCell colSpan={7} className="tableCell">Error loading data</TableCell>
            </TableRow>
          ) : (
            data.map((booking) => (
              <TableRow key={booking._id}>
                <TableCell className="tableCell">{booking?.bookingId}</TableCell>
                <TableCell className="tableCell">
                  <div className="cellWrapper">
                    <img src={booking?.roomResponse?.hotelResponse?.photos} alt="" className="image" />
                    {booking?.roomResponse?.hotelResponse?.name}
                  </div>
                </TableCell>
                <TableCell className="tableCell">{booking?.roomResponse?.roomNumber}</TableCell>
                <TableCell className="tableCell">{new Date(booking?.checkInDate).toDateString()}</TableCell>
                <TableCell className="tableCell">{new Date(booking?.checkOutDate).toDateString()}</TableCell>
                <TableCell className="tableCell">${booking?.totalPrice}</TableCell>
              </TableRow>
            ))
          )}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default List;
