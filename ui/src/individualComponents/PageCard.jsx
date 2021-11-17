import React from "react";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";

const PageCard = (props) => {
  return (
    <div className={"pt-3 animate__animated animate__fadeIn"}>
      <Card>
        <CardContent>{props.render}</CardContent>
      </Card>
    </div>
  );
};

export default PageCard;
