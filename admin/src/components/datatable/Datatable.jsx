import "./datatable.scss";
import { DataGrid } from "@mui/x-data-grid";
import { Link, useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import useFetch from "../../hooks/useFetch";
import axios from "axios";
import { Button } from "@mui/material";

const Datatable = ({ columns }) => {
  const location = useLocation();
  const path = location.pathname.split("/")[1];
  const [list, setList] = useState([]);
  const { data, loading } = useFetch(`/${path}`);
  const [banStatus, setBanStatus] = useState('ban');
  console.log('data >> ', data)
  useEffect(() => {
    setList(data || []);
  }, [data]);

  const handleDelete = async (id, hotelId) => {
    try {
      let idField = "userId";
      if(path === 'rooms') {
        idField = "roomId";
      }
      if(path === 'hotels') {
        idField = "id";
      }
      const deleteUrl = `/${path}/${id}${path === "rooms" ? `/${hotelId}` : ""}`;
      await axios.delete(deleteUrl);
      setList((prevList) => prevList.filter((item) => item[idField] !== id));
    } catch (err) {
      console.error(err);
    }
  };

  const handleBanUnban = async (id) => {
    try {
      const res = await axios.post(`/auth/${banStatus}User?userId=${id}`);
      console.log('res >> ', res.data.message)
      if(banStatus === 'ban') {
        setBanStatus('unban');
      } else {
        setBanStatus('ban');
      }
      setList((prevList) =>
        prevList.map((user) =>
          user.userId === id ? { ...user, banned: !user.banned } : user
        )
      );
    } catch (err) {
      console.error(err);
      alert('User has bookings, cannot ban.')
    }
  };

  const actionColumn = [
    {
      field: "action",
      headerName: "Action",
      width: 200,
      renderCell: (params) => (
        <div className="cellAction">
          <Link
            to={`/${path}${path === "hotels" ? "/find" : ""}/${params.row[path === "hotels" ? "id" : path === "rooms" ? "roomId" : "userId"]}`}
            style={{ textDecoration: "none" }}
          >
            <div className="viewButton">View</div>
          </Link>

          {path === "users" ? (
          <Button
            variant="contained"
            style={{
              backgroundColor: !params.row?.banned ? "#FFCDD2" : "#C8E6C9",
              color: !params.row?.banned ? "#D32F2F" : "#388E3C",
              width: "100px",
              height: "36px",
              fontSize: "14px",
              textTransform: "none",
            }}
            onClick={() => handleBanUnban(params.row.userId)}
          >
            {!params.row?.banned ? "Ban" : "Unban"}
          </Button>
        ) : path === "rooms" ? (
          <Button
            variant="contained"
            style={{
              backgroundColor: "#FFCDD2",
              color: "#D32F2F",
              width: "100px",
              height: "36px",
              fontSize: "14px",
              textTransform: "none",
            }}
            onClick={() => handleDelete(params.row.roomId, params.row.hotelId)}
          >
            Delete
          </Button>
        ) : (
          <Button
            variant="contained"
            style={{
              backgroundColor: "#FFCDD2",
              color: "#D32F2F",
              width: "100px",
              height: "36px",
              fontSize: "14px",
              textTransform: "none",
            }}
            onClick={() => handleDelete(params.row.userId)}
          >
            Delete
          </Button>
        )}
        </div>
      ),
    },
  ];

  const filteredColumns = columns.filter(col => col.field !== "_id");

  return (
    <div className="datatable">
      <div className="datatableTitle">
        List {path}
        <Link to={`/${path}/new`} className="link">
          Add New
        </Link>
      </div>

      <DataGrid
        className="datagrid"
        rows={list}
        columns={filteredColumns.concat(actionColumn)}
        pageSize={9}
        rowsPerPageOptions={[9]}
        getRowId={(row) => {
          if(path === 'rooms') {
            return row["roomId"];
          }
          if(path === 'hotels') {
            return row["id"]
          }
          return row["userId"];
        }}
        disableSelectionOnClick
        checkboxSelection={false}
      />
    </div>
  );
};


export default Datatable;
