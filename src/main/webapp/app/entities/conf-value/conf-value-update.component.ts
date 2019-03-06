import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IConfValue } from 'app/shared/model/conf-value.model';
import { ConfValueService } from './conf-value.service';
import { IConfig } from 'app/shared/model/config.model';
import { ConfigService } from 'app/entities/config';

@Component({
    selector: 'jhi-conf-value-update',
    templateUrl: './conf-value-update.component.html'
})
export class ConfValueUpdateComponent implements OnInit {
    confValue: IConfValue;
    isSaving: boolean;

    configs: IConfig[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected confValueService: ConfValueService,
        protected configService: ConfigService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ confValue }) => {
            this.confValue = confValue;
        });
        this.configService.query().subscribe(
            (res: HttpResponse<IConfig[]>) => {
                this.configs = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.confValue.id !== undefined) {
            this.subscribeToSaveResponse(this.confValueService.update(this.confValue));
        } else {
            this.subscribeToSaveResponse(this.confValueService.create(this.confValue));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IConfValue>>) {
        result.subscribe((res: HttpResponse<IConfValue>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackConfigById(index: number, item: IConfig) {
        return item.id;
    }
}
