import "./userTopList.css";
import AuthorCard from "../authorCard/AuthorCard";
const UserTopList = () => {
  return (
    <div className="uTopList">
        <div className="userTopContent">
          <h1 className="userTopTitle">Top 10 author of the month</h1>
          <p className="userTopDetail">Rating based on customer reviews</p>
        </div>
      <div className="pList">
          <AuthorCard/>
          <AuthorCard/>
          <AuthorCard/>
          <AuthorCard/>
      </div>
      <div className="pList">
          <AuthorCard/>
          <AuthorCard/>
          <AuthorCard/>
          <AuthorCard/>
      </div>
    </div>
  );
};

export default UserTopList;
