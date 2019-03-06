import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IConfig } from 'app/shared/model/config.model';
import { ConfigService } from './config.service';
import { ISmartObject } from 'app/shared/model/smart-object.model';
import { SmartObjectService } from 'app/entities/smart-object';
import { IProfileConfValue } from 'app/shared/model/profile-conf-value.model';
import { ProfileConfValueService } from 'app/entities/profile-conf-value';

@Component({
    selector: 'jhi-config-update',
    templateUrl: './config-update.component.html'
})
export class ConfigUpdateComponent implements OnInit {
    config: IConfig;
    isSaving: boolean;

    smartobjects: ISmartObject[];

    profileconfvalues: IProfileConfValue[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected configService: ConfigService,
        protected smartObjectService: SmartObjectService,
        protected profileConfValueService: ProfileConfValueService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ config }) => {
            this.config = config;
        });
        this.smartObjectService.query().subscribe(
            (res: HttpResponse<ISmartObject[]>) => {
                this.smartobjects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.profileConfValueService.query().subscribe(
            (res: HttpResponse<IProfileConfValue[]>) => {
                this.profileconfvalues = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.config.id !== undefined) {
            this.subscribeToSaveResponse(this.configService.update(this.config));
        } else {
            this.subscribeToSaveResponse(this.configService.create(this.config));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IConfig>>) {
        result.subscribe((res: HttpResponse<IConfig>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackSmartObjectById(index: number, item: ISmartObject) {
        return item.id;
    }

    trackProfileConfValueById(index: number, item: IProfileConfValue) {
        return item.id;
    }
}
