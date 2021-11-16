import * as React from "react";
import Head from "next/head";
import Typography from "@mui/material/Typography";

export default function Index() {
  return (
    <>
      <Head>
        <title>Inside Alert</title>
      </Head>
      <div className={"container"}>
        <div className={"row"}>
          <div className={"col"}>
            <Typography variant="h1" component="div" gutterBottom>
              Inside Alert
            </Typography>
          </div>
        </div>
      </div>
    </>
  );
}
