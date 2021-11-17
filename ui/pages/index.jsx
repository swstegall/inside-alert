import * as React from "react";
import Head from "next/head";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import PageCard from "../src/individualComponents/PageCard";
import ExampleChart from "../src/individualComponents/ExampleChart";

export default function Index() {
  const [apiKeyID, setAPIKeyID] = React.useState("");
  const [secretKey, setSecretKey] = React.useState("");

  return (
    <>
      <Head>
        <title>Inside Alert</title>
      </Head>
      <AppBar position="static">
        <Toolbar variant="dense">
          <Typography variant="h6" color="inherit" component="div">
            Inside Alert
          </Typography>
        </Toolbar>
      </AppBar>
      <div className={"container"}>
        <PageCard
          render={
            <>
              <div className={"row pt-4"}>
                <div className={"col"}>
                  <TextField
                    fullWidth
                    id="api-key-id"
                    label="API Key ID"
                    value={apiKeyID}
                    onChange={(e) => setAPIKeyID(e.target.value)}
                  />
                </div>
                <div className={"col"}>
                  <TextField
                    fullWidth
                    id="secret-key"
                    label="Secret Key"
                    value={secretKey}
                    onChange={(e) => setSecretKey(e.target.value)}
                  />
                </div>
                <div
                  className={
                    "col d-flex align-items-center justify-content-flex-begin"
                  }
                >
                  <Button variant="contained">Request Info</Button>
                </div>
              </div>
              <div className={"row pt-4"}>
                <div className={"col"}>
                  <ExampleChart />
                </div>
              </div>
            </>
          }
        />
      </div>
    </>
  );
}
