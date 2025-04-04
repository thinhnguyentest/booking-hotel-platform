import { Avatar, Dropdown, Space } from 'antd';
import { UserOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { logout } from '../../redux/features/authSlice';



const DropDownAccount = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const handleLogout = () => {
    dispatch(logout());
    navigate("/")
}

  const items = [
  {
    key: '1',
    label: 'My Account',
    disabled: true,
  },
  {
    type: 'divider',
  },
  {
    key: '2',
    label: 'Profile',
    extra: '⌘P',
  },
  {
    key: '3',
    label: 'Logout',
    extra: '⌘L',
    onClick: handleLogout,
  },
];

  return (
    <Dropdown
      menu={{
        items,
        style: { // Inline style cho dropdown menu
          borderRadius: '8px',
          boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)',
        },
      }}
    >
      <a href= "##" onClick={(e) => e.preventDefault()}>
        <Space>
          <Avatar
            icon ={<UserOutlined/>}
            style={{ // Inline style cho avatar
              cursor: 'pointer',
              transition: 'transform 0.3s ease',
            }}
            onMouseEnter={(e) => (e.currentTarget.style.transform = 'scale(1.1)')}
            onMouseLeave={(e) => (e.currentTarget.style.transform = 'scale(1)')}
          />
        </Space>
      </a>
    </Dropdown>
  );
};

export default DropDownAccount;
