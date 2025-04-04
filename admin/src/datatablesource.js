export const userColumns = [
  { field: "_id", headerName: "ID", width: 70 },
  {
    field: "user",
    headerName: "User",
    width: 230,
    renderCell: (params) => {
      return (
        <div className="cellWithImg">
          <img className="cellImg" src={params.row.img || "https://i.ibb.co/MBtjqXQ/no-avatar.gif"} alt="avatar" />
          {params.row.username}
        </div>
      );
    },
  },
  {
    field: "email",
    headerName: "Email",
    width: 230,
  },

  {
    field: "city",
    headerName: "City",
    width: 100,
  },
  {
    field: "phone",
    headerName: "Phone",
    width: 100,
  },
];

export const hotelColumns = [
  { field: "_id", headerName: "ID", width: 250 },
  {
    field: "name",
    headerName: "Name",
    width: 150,
  },
  {
    field: "address",
    headerName: "Address",
    width: 230,
  },
  {
    field: "cheapestPrice",
    headerName: "Cheapest Price",
    width: 100,
  },
  {
    field: "city",
    headerName: "City",
    width: 100,
  },
];

export const roomColumns = [
  { field: "_id", headerName: "ID", width: 70 },
  {
    field: "roomType",
    headerName: "Room Type",
    width: 230,
  },
  {
    field: "roomNumber",
    headerName: "Room Number",
    width: 200,
  },
  {
    field: "price",
    headerName: "Price",
    width: 100,
  },
];

export const voucherColumns = [
  { field: "_id", headerName: "ID", width: 70 },
  {
    field: "code",
    headerName: "Code",
    width: 150,
  },
  {
    field: "discount",
    headerName: "Discount (%)",
    width: 100,
  },
  {
    field: "startDate",
    headerName: "Start Date",
    width: 150,
    valueGetter: (params) => new Date(params.row.startDate).toLocaleDateString(), // Định dạng ngày
  },
  {
    field: "endDate",
    headerName: "End Date",
    width: 150,
    valueGetter: (params) => new Date(params.row.endDate).toLocaleDateString(), // Định dạng ngày
  },
  {
    field: "status",
    headerName: "Status",
    width: 100,
    valueGetter: (params) => (params.row.status ? "Active" : "Inactive"), // Hiển thị trạng thái
  },
];

